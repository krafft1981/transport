package com.rental.transport.service;

import com.rental.transport.dto.Parking;
import com.rental.transport.dto.Property;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.enums.PropertyNameEnum;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.ParkingMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.ValidatorFactory;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ParkingService {

    private final ParkingRepository parkingRepository;
    private final CustomerService customerService;
    private final ImageService imageService;
    private final PropertyService propertyService;
    private final ParkingMapper parkingMapper;
    private final ValidatorFactory vf;

    public void delete(String account, Long id)
            throws AccessDeniedException, ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        ParkingEntity parking = getEntity(id);
        if (!parking.getCustomer().contains(customer))
            throw new AccessDeniedException("Delete");

        if (!parking.getTransport().isEmpty())
            throw new IllegalArgumentException("Removal of non-empty parking is prohibited");

        parkingRepository.delete(parking);
    }

    public Long create(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        ParkingEntity parking = new ParkingEntity(customer);
        parking.addProperty(
                propertyService.create(PropertyNameEnum.PARKING_NAME, "Название не указано"),
                propertyService.create(PropertyNameEnum.PARKING_LATITUDE, "0"),
                propertyService.create(PropertyNameEnum.PARKING_LONGITUDE, "0"),
                propertyService.create(PropertyNameEnum.PARKING_ADDRESS, "Не указан"),
                propertyService.create(PropertyNameEnum.PARKING_LOCALITY, "Не указан"),
                propertyService.create(PropertyNameEnum.PARKING_REGION, "Не указан"),
                propertyService.create(PropertyNameEnum.PARKING_DESCRIPTION, "Место для отдыха")
        );

        return parkingRepository.save(parking).getId();
    }

    public Parking update(String account, Parking parking)
            throws AccessDeniedException, ObjectNotFoundException, IllegalArgumentException {

        for (Property property : parking.getProperty()) {
            if (!vf.getValidator(PropertyTypeEnum.valueOf(property.getType())).validate(property.getValue()))
                throw new IllegalArgumentException("Неправильное значение поля: '" + property.getHumanName() + "'");
        }

        ParkingEntity entity = parkingMapper.toEntity(parking);
        CustomerEntity customer = customerService.getEntity(account);

        if (!entity.getCustomer().contains(customer))
            throw new AccessDeniedException("Change");

        parkingRepository.save(entity);
        return parkingMapper.toDto(entity);
    }

    public List<Parking> getMyParking(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return parkingRepository.findAllByCustomerId(customer.getId())
                .parallelStream()
                .map(entity -> parkingMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Parking> getPage(Pageable pageable) {

        return parkingRepository
                .findAllByEnableTrue(pageable)
                .parallelStream()
                .map(entity -> parkingMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public ParkingEntity getEntity(Long id) throws ObjectNotFoundException {

        return parkingRepository
                .findById(id)
                .filter(ParkingEntity::getEnable)
                .orElseThrow(() -> new ObjectNotFoundException("Parking", id));
    }

    public Parking getDto(Long id) throws ObjectNotFoundException {

        return parkingMapper.toDto(getEntity(id));
    }

    @Transactional
    public Parking addParkingImage(String account, Long parkingId, byte[] data)
            throws AccessDeniedException, ObjectNotFoundException {

        ParkingEntity parking = getEntity(parkingId);
        parking.addImage(new ImageEntity(data));
        parkingRepository.save(parking);
        return parkingMapper.toDto(parking);
    }

    @Transactional
    public Parking delParkingImage(String account, Long parkingId, Long imageId)
            throws AccessDeniedException, ObjectNotFoundException {

        ParkingEntity parking = getEntity(parkingId);
        parking.delImage(imageService.getEntity(imageId));
        parkingRepository.save(parking);
        return parkingMapper.toDto(parking);
    }
}

package com.rental.transport.service;

import com.rental.transport.dto.Parking;
import com.rental.transport.dto.Property;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.ParkingMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransportService transportService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ParkingMapper parkingMapper;

    private ValidatorFactory vf = new ValidatorFactory();

    public void delete(String account, Long id)
            throws AccessDeniedException, ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        ParkingEntity parking = getEntity(id);
        if (parking.getCustomer().contains(customer) == false)
            throw new AccessDeniedException("Delete");

        if (!parking.getTransport().isEmpty())
            throw new IllegalArgumentException("Removal of non-empty parking is prohibited");

        parkingRepository.delete(parking);
    }

    public Long create(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        ParkingEntity parking = new ParkingEntity(customer);
        parking.addProperty(
                propertyService.create("parking_name", "Название не указано"),
                propertyService.create("parking_latitude", "0"),
                propertyService.create("parking_longitude", "0"),
                propertyService.create("parking_address", "Не указан"),
                propertyService.create("parking_locality", "Не указан"),
                propertyService.create("parking_region", "Не указан"),
                propertyService.create("parking_description", "Место для отдыха")
        );

        return parkingRepository.save(parking).getId();
    }

    public Parking update(String account, Parking parking)
            throws AccessDeniedException, ObjectNotFoundException, IllegalArgumentException {

        for (Property property : parking.getProperty()) {
            if (!vf.getValidator(property.getType()).validate(property.getValue()))
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
                .stream()
                .map(entity -> parkingMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Parking> getPage(Pageable pageable) {

        return parkingRepository
                .findAllByEnableTrue(pageable)
                .stream()
                .map(entity -> parkingMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public ParkingEntity getEntity(Long id) throws ObjectNotFoundException {

        return parkingRepository
                .findById(id)
                .filter(entity -> entity.getEnable())
                .orElseThrow(() -> new ObjectNotFoundException("Parking", id));
    }

    public Parking getDto(Long id) throws ObjectNotFoundException {

        return parkingMapper.toDto(getEntity(id));
    }

    @Transactional
    public Parking addParkingImage(String account, Long parkingId, byte[] data)
            throws AccessDeniedException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        ParkingEntity parking = getEntity(parkingId);
        parking.addImage(new ImageEntity(data));
        parkingRepository.save(parking);
        return parkingMapper.toDto(parking);
    }

    @Transactional
    public Parking delParkingImage(String account, Long parkingId, Long imageId)
            throws AccessDeniedException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        ParkingEntity parking = getEntity(parkingId);
        parking.delImage(imageService.getEntity(imageId));
        parkingRepository.save(parking);
        return parkingMapper.toDto(parking);
    }

    @PostConstruct
    public void postConstruct() {

        propertyService.createType("parking_name", "Название", PropertyTypeEnum.String);
        propertyService.createType("parking_latitude", "Широта", PropertyTypeEnum.Double);
        propertyService.createType("parking_longitude", "Долгота", PropertyTypeEnum.Double);
        propertyService.createType("parking_address", "Адрес", PropertyTypeEnum.String);
        propertyService.createType("parking_locality", "Местонахождение", PropertyTypeEnum.String);
        propertyService.createType("parking_region", "Район", PropertyTypeEnum.String);
        propertyService.createType("parking_description", "Описание", PropertyTypeEnum.String);
    }
}

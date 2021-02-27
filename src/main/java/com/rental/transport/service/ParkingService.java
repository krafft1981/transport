package com.rental.transport.service;

import com.rental.transport.dto.Parking;
import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.mapper.ParkingMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    public void delete(@NonNull String account, @NonNull Long id)
            throws AccessDeniedException, ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        ParkingEntity parking = getEntity(id);
        if (parking.getCustomer().contains(customer) == false)
            throw new AccessDeniedException("Delete");

        if (!parking.getTransport().isEmpty())
            throw new IllegalArgumentException("Removal of non-empty parking is prohibited");

        parkingRepository.delete(parking);
    }

    public Long create(@NonNull String account) throws ObjectNotFoundException {

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

    public void update(@NonNull String account, @NonNull Parking dto)
            throws AccessDeniedException, ObjectNotFoundException {

        ParkingEntity entity = parkingMapper.toEntity(dto);
        CustomerEntity customer = customerService.getEntity(account);

        if (!entity.getCustomer().contains(customer))
            throw new AccessDeniedException("Change");

        parkingRepository.save(entity);
    }

    public List<Parking> getMyParking(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return parkingRepository.findAllByCustomerId(customer.getId())
                .stream()
                .map(entity -> {
                    return parkingMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public List<Parking> getPage(Pageable pageable) {

        return parkingRepository
                .findAllByEnableTrue(pageable)
                .stream()
                .map(entity -> {
                    return parkingMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public Long count() {

        return parkingRepository.count();
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
}

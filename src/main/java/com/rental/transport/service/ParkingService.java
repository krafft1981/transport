package com.rental.transport.service;

import com.rental.transport.dto.Parking;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
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
public class ParkingService extends PropertyService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransportService transportService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ParkingMapper mapper;

    public void delete(@NonNull String account, @NonNull Long id)
            throws AccessDeniedException, ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerRepository.findByAccount(account);
        ParkingEntity parking = parkingRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Parking", id));

        if (parking.getCustomer().contains(customer) == false)
            throw new AccessDeniedException("Delete");

        if (!parking.getTransport().isEmpty())
            throw new IllegalArgumentException("Removal of non-empty parking is prohibited");

        parkingRepository.delete(parking);
    }

    public Long create(@NonNull String account) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        ParkingEntity entity = new ParkingEntity(customer);
        return parkingRepository.save(entity).getId();
    }

    public void update(@NonNull String account, @NonNull Parking dto)
            throws AccessDeniedException, ObjectNotFoundException {

        ParkingEntity parking = parkingRepository
                .findById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Parking", dto.getId()));

        parking = mapper.toEntity(dto);
        CustomerEntity customer = customerRepository.findByAccount(account);

        if (parking.getCustomer().contains(customer) == false)
            throw new AccessDeniedException("Change");

        parkingRepository.save(parking);
    }

    public List<Parking> getPage(Pageable pageable) {

        return parkingRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public Long count() {

        Long count = parkingRepository.count();
        return count;
    }
}

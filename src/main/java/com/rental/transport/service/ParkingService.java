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
public class ParkingService {

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
            throws AccessDeniedException {

        CustomerEntity customer = customerRepository.findByAccount(account);
        ParkingEntity parking = parkingRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Стоянка", id));

        if (parking.getCustomer().contains(customer) == false) {
            throw new AccessDeniedException("Удаление");
        }

        parkingRepository.delete(parking);
    }

    public Long create(@NonNull String account) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        ParkingEntity parking = mapper.create();
        parking.addCustomer(customer);
        return parkingRepository.save(parking).getId();
    }

    public void update(@NonNull String account, @NonNull Parking dto) {

        ParkingEntity parking = parkingRepository
                .findById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("Стоянка", dto.getId()));

        parking = mapper.toEntity(dto);
        CustomerEntity customer = customerRepository.findByAccount(account);

        if (parking.getCustomer().contains(customer) == false) {
            throw new AccessDeniedException("Изменение");
        }

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
}

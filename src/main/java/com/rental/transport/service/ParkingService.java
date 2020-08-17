package com.rental.transport.service;

import com.rental.transport.dao.CustomerEntity;
import com.rental.transport.dao.CustomerRepository;
import com.rental.transport.dao.ParkingEntity;
import com.rental.transport.dao.ParkingRepository;
import com.rental.transport.dao.TransportRepository;
import com.rental.transport.dto.Parking;
import com.rental.transport.utils.exceptions.CustomerNotFoundException;
import com.rental.transport.utils.exceptions.ParkingNotFoundException;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

@Service
public class ParkingService {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransportRepository transportRepository;

    public Parking findById(@NonNull Long id) throws ParkingNotFoundException {

        ParkingEntity entity = parkingRepository
                .findById(id)
                .orElseThrow(() -> new ParkingNotFoundException(id));

        Parking parking = new Parking(
                entity.getId(),
                entity.getAddress(),
                entity.getDescription(),
                new HashSet<>(),
                new HashSet<>()
        );

        entity.getTransport().stream().forEach(transport -> {
            parking.addTransport(transport.getId());
        });
        return parking;
    }

    public void setParking(@NonNull String account, @NonNull Parking parking) throws ParkingNotFoundException {

        CustomerEntity customerEntity = customerRepository.findByAccount(account);
//        if (parking.getCustomerId().equals(customerEntity.getId())) {
//            return;
//        }

        ParkingEntity parkingEntity = parkingRepository
                .findById(parking.getId())
                .orElseThrow(() -> new ParkingNotFoundException(parking.getId()));

        parkingEntity.setAddress(parking.getAddress());
        parkingEntity.setDescription(parking.getDescription());

        parkingRepository.save(parkingEntity);
    }

    public Long newParking(@NonNull String account) {

        ParkingEntity parking = new ParkingEntity();
//        parking.setCustomer(customerRepository.findByAccount(account).getId());
        parking = parkingRepository.save(parking);
        return parking.getId();
    }

    public List<Parking> getList(@NonNull Integer page, @NonNull Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
  /*
        return parkingRepository.findAll(pageable).stream()
                .map(entity -> {
                    Parking parking = new Parking(
                            entity.getId(),
                            entity.getAddress(),
                            customerRepository
                                    .findById(entity.getId())
                                    .orElseThrow(() -> new CustomerNotFoundException(entity.getId())).getId(),
                            entity.getDescription(),
                            new HashSet<>()
                    );

                    return parking;
                })
                .collect(Collectors.toList());
*/
        return null;
    }
}

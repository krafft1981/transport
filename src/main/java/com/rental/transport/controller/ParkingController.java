package com.rental.transport.controller;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.dto.Parking;
import com.rental.transport.mapper.ParkingMapper;
import com.rental.transport.service.ParkingService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/parking")
@RestController
public class ParkingController extends
        AbstractController<ParkingEntity, Parking, ParkingRepository, ParkingMapper, ParkingService> {

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public ParkingController(ParkingService service) {
        super(service);
    }

    @Override
    public Long create(Principal principal) {

        CustomerEntity customer = customerRepository.findByAccount(principal.getName());
        ParkingEntity parking = new ParkingEntity(customer);
        return parkingRepository.save(parking).getId();
    }
}

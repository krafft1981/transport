package com.rental.transport.controller;

import com.rental.transport.dao.ParkingEntity;
import com.rental.transport.dao.ParkingRepository;
import com.rental.transport.dto.Parking;
import com.rental.transport.service.ParkingMapper;
import com.rental.transport.service.ParkingService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/parking")
@RestController
public class ParkingController extends
        AbstractController<ParkingEntity, Parking, ParkingRepository, ParkingMapper, ParkingService> {

    public ParkingController(ParkingService service) {
        super(service);
    }
}

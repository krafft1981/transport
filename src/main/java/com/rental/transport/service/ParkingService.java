package com.rental.transport.service;

import com.rental.transport.dao.ParkingEntity;
import com.rental.transport.dao.ParkingRepository;
import com.rental.transport.dto.Parking;
import org.springframework.stereotype.Service;

@Service
public class ParkingService extends AbstractService<ParkingEntity, ParkingRepository, ParkingMapper, Parking> {

    public ParkingService(ParkingRepository repository, ParkingMapper mapper) {
        super(repository, mapper);
    }
}

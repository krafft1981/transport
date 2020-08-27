package com.rental.transport.service;

import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.dto.Parking;
import com.rental.transport.mapper.ParkingMapper;
import org.springframework.stereotype.Service;

@Service
public class ParkingService extends AbstractService<ParkingEntity, ParkingRepository, ParkingMapper, Parking> {

    public ParkingService(ParkingRepository repository, ParkingMapper mapper) {
        super(repository, mapper);
    }
}

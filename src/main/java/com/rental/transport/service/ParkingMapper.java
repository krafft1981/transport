package com.rental.transport.service;

import com.rental.transport.dao.ParkingEntity;
import com.rental.transport.dao.TransportEntity;
import com.rental.transport.dto.Parking;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ParkingMapper implements AbstractMapper<ParkingEntity, Parking>{

    @Autowired
    private ModelMapper mapper;

    public ParkingEntity toEntity(Parking dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, ParkingEntity.class);
    }

    public Parking toDto(ParkingEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Parking.class);
    }

    @Override
    public ParkingEntity create() {
        ParkingEntity entity = new ParkingEntity();
        return entity;
    }
}

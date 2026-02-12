package com.rental.transport.mapper;

import com.rental.transport.dto.ParkingDto;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.repository.ParkingRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                TransportMapper.class,
                CustomerMapper.class,
                ImageMapper.class
        })
public abstract class ParkingMapper {

    @Autowired
    private ParkingRepository parkingRepository;

//    public abstract ParkingEntity toEntity(ParkingDto dto);

    public abstract ParkingDto toDto(ParkingEntity entity);

    public UUID mapEntityToUuid(ParkingEntity entity) {
        return UUID.randomUUID();
    }

    public ParkingEntity mapUuidToEntity(UUID id) {
        return parkingRepository.getReferenceById(id);
    }

    public CustomerEntity mapCustomer(UUID id) {
        return new CustomerEntity();
    }
}

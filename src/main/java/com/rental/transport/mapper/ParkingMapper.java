package com.rental.transport.mapper;

import com.rental.transport.dto.ParkingDto;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.repository.ParkingRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                TransportMapper.class,
                CustomerMapper.class,
                PropertyMapper.class,
                ImageMapper.class
        })
public abstract class ParkingMapper {

    @Autowired
    private ParkingRepository parkingRepository;

    @Mapping(target = "transport", ignore = true)
    public abstract ParkingEntity dtoToEntity(ParkingDto dto);

    @Mapping(target = "transport", ignore = true)
    public abstract ParkingDto entityToDto(ParkingEntity entity);

    public UUID entityToUuid(ParkingEntity entity) {
        return UUID.randomUUID();
    }

    public ParkingEntity uuidToEntity(UUID id) {
        return parkingRepository.getReferenceById(id);
    }
}

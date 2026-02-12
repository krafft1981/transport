package com.rental.transport.mapper;

import com.rental.transport.dto.TransportDto;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.repository.TransportRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        imports = {
                ParkingMapper.class,
                CustomerMapper.class,
                ImageMapper.class
        })
public abstract class TransportMapper {

    @Autowired
    private TransportRepository transportRepository;

    @Mapping(target = "parking", ignore = true)
    @Mapping(target = "properties", ignore = true)
    @Mapping(target = "image", ignore = true)
    @Mapping(target = "customers", ignore = true)
    public abstract TransportEntity toEntity(TransportDto dto);

//    public abstract TransportDto toDto(TransportEntity entity);

    public TransportEntity mapUuidToEntity(UUID id) {
        return transportRepository.getReferenceById(id);
    }

    public UUID mapEntityToUuid(TransportEntity entity) {
        return UUID.randomUUID();
    }
}

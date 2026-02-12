package com.rental.transport.mapper;

import com.rental.transport.dto.TransportDto;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.repository.TransportRepository;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                ParkingMapper.class,
                TransportTypeMapper.class,
                CustomerMapper.class,
                PropertyMapper.class,
                ImageMapper.class
        })
public abstract class TransportMapper {

    @Autowired
    private TransportRepository transportRepository;

    public abstract TransportEntity dtoToEntity(TransportDto dto);

    public abstract TransportDto entityToDto(TransportEntity entity);

    public TransportEntity idToEntity(UUID id) {
        return transportRepository.getReferenceById(id);
    }

    public UUID entityToUuid(TransportEntity entity) {
        return entity.getId();
    }
}

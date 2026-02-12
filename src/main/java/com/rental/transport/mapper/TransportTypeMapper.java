package com.rental.transport.mapper;

import com.rental.transport.dto.TransportTypeDto;
import com.rental.transport.entity.TransportTypeEntity;
import com.rental.transport.repository.ImageRepository;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TransportTypeMapper {

    @Autowired
    private ImageRepository repository;

    public abstract TransportTypeEntity dtoToEntity(TransportTypeDto dto);

    public abstract TransportTypeDto entityToDto(TransportTypeEntity entity);
}

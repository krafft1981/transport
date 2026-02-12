package com.rental.transport.mapper;

import com.rental.transport.dto.TransportTypeDto;
import com.rental.transport.entity.TransportTypeEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransportTypeMapper {

    TransportTypeEntity toEntity(TransportTypeDto dto);
    TransportTypeDto toDto(TransportTypeEntity entity);
}

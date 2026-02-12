package com.rental.transport.mapper;

import com.rental.transport.dto.PropertyDto;
import com.rental.transport.entity.PropertyEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyMapper {

    PropertyEntity toEntity(PropertyDto dto);
    PropertyDto toDto(PropertyEntity entity);
}

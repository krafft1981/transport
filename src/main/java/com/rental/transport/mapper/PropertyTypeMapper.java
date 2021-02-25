package com.rental.transport.mapper;

import com.rental.transport.dto.PropertyType;
import com.rental.transport.entity.PropertyTypeEntity;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertyTypeMapper implements AbstractMapper<PropertyTypeEntity, PropertyType> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public PropertyTypeEntity toEntity(PropertyType dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, PropertyTypeEntity.class);
    }

    @Override
    public PropertyType toDto(PropertyTypeEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, PropertyType.class);
    }
}
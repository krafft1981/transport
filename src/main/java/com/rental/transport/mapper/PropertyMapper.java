package com.rental.transport.mapper;

import com.rental.transport.dto.Property;
import com.rental.transport.entity.PropertyEntity;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertyMapper implements AbstractMapper<PropertyEntity, Property> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public PropertyEntity toEntity(Property dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, PropertyEntity.class);
    }

    @Override
    public Property toDto(PropertyEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Property.class);
    }
}

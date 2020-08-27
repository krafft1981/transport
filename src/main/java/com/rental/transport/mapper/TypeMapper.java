package com.rental.transport.mapper;

import com.rental.transport.entity.TypeEntity;
import com.rental.transport.dto.Type;
import com.rental.transport.mapper.AbstractMapper;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TypeMapper implements AbstractMapper<TypeEntity, Type> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public TypeEntity toEntity(Type dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, TypeEntity.class);
    }

    @Override
    public Type toDto(TypeEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Type.class);
    }

    @Override
    public TypeEntity create() {
        TypeEntity entity = new TypeEntity();
        return entity;
    }
}

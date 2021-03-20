package com.rental.transport.mapper;

import com.rental.transport.dto.Type;
import com.rental.transport.entity.TransportTypeEntity;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TypeMapper implements AbstractMapper<TransportTypeEntity, Type> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public TransportTypeEntity toEntity(Type dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, TransportTypeEntity.class);
    }

    @Override
    public Type toDto(TransportTypeEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Type.class);
    }
}

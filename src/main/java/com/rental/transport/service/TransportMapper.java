package com.rental.transport.service;

import com.rental.transport.dao.TransportEntity;
import com.rental.transport.dao.TypeEntity;
import com.rental.transport.dto.Transport;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransportMapper implements AbstractMapper<TransportEntity, Transport>{

    @Autowired
    private ModelMapper mapper;

    @Override
    public TransportEntity toEntity(Transport dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, TransportEntity.class);
    }

    @Override
    public Transport toDto(TransportEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Transport.class);
    }

    @Override
    public TransportEntity create() {
        TransportEntity entity = new TransportEntity();
        return entity;
    }
}

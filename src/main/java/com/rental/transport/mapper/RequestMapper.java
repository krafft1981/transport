package com.rental.transport.mapper;

import com.rental.transport.dto.Request;
import com.rental.transport.entity.RequestEntity;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RequestMapper implements AbstractMapper<RequestEntity, Request> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public RequestEntity toEntity(Request dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, RequestEntity.class);
    }

    @Override
    public Request toDto(RequestEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Request.class);
    }
}

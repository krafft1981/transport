package com.rental.transport.mapper;

import com.rental.transport.dto.Templates;
import com.rental.transport.entity.TemplatesEntity;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TemplatesMapper implements AbstractMapper<TemplatesEntity, Templates> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public TemplatesEntity toEntity(Templates dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, TemplatesEntity.class);
    }

    @Override
    public Templates toDto(TemplatesEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Templates.class);
    }
}

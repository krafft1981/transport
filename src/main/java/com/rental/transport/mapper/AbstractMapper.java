package com.rental.transport.mapper;

import com.rental.transport.entity.AbstractEntity;
import com.rental.transport.dto.AbstractDto;

public interface AbstractMapper<E extends AbstractEntity, D extends AbstractDto> {

    E toEntity(D dto);
    D toDto(E entity);
    E create();
}

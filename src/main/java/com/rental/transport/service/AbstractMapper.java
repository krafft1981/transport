package com.rental.transport.service;

import com.rental.transport.dao.AbstractEntity;
import com.rental.transport.dto.AbstractDto;

public interface AbstractMapper<E extends AbstractEntity, D extends AbstractDto> {

    E toEntity(D dto);
    D toDto(E entity);
    E create();
}

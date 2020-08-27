package com.rental.transport.service;

import com.rental.transport.entity.TypeEntity;
import com.rental.transport.entity.TypeRepository;
import com.rental.transport.dto.Type;
import com.rental.transport.mapper.TypeMapper;
import org.springframework.stereotype.Service;

@Service
public class TypeService extends AbstractService<TypeEntity, TypeRepository, TypeMapper, Type> {

    public TypeService(TypeRepository repository, TypeMapper mapper) {
        super(repository, mapper);
    }
}

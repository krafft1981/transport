package com.rental.transport.service;

import com.rental.transport.dao.TypeEntity;
import com.rental.transport.dao.TypeRepository;
import com.rental.transport.dto.Type;
import org.springframework.stereotype.Service;

@Service
public class TypeService extends AbstractService<TypeEntity, TypeRepository, TypeMapper, Type> {

    public TypeService(TypeRepository repository, TypeMapper mapper) {
        super(repository, mapper);
    }
}

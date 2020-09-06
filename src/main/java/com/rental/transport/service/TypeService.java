package com.rental.transport.service;

import com.rental.transport.dto.Type;
import com.rental.transport.entity.TypeEntity;
import com.rental.transport.entity.TypeRepository;
import com.rental.transport.mapper.TypeMapper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TypeService {

    @Autowired
    private TypeRepository repository;

    @Autowired
    private TypeMapper mapper;

    public Long create(String name) throws IllegalArgumentException {

        TypeEntity entity = mapper.create();
        entity.setName(name);
        return repository.save(entity).getId();
    }

    public Long count() {

        Long count = repository.count();
        return count;
    }

    public List<Type> getPage(Pageable pageable) {

        return repository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }
}

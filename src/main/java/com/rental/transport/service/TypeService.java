package com.rental.transport.service;

import com.rental.transport.dto.Type;
import com.rental.transport.entity.TypeEntity;
import com.rental.transport.entity.TypeRepository;
import com.rental.transport.mapper.TypeMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.Objects;
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

        TypeEntity entity = new TypeEntity(name);
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

    public TypeEntity get(Long id) throws ObjectNotFoundException {

        return repository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Type", id));
    }

    public TypeEntity get(String name) throws ObjectNotFoundException {

        TypeEntity entity = repository.findByName(name);
        if (Objects.isNull(entity)) {
            throw new ObjectNotFoundException("Type", name);
        }
        return entity;
    }
}

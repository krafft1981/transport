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
    private TypeMapper typeMapper;

    public Long create(String name) throws IllegalArgumentException {

        TypeEntity entity = new TypeEntity(name);
        return repository.save(entity).getId();
    }

    public Long count() {

        return repository.count();
    }

    public List<Type> getPage(Pageable pageable) {

        return repository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> {
                    return typeMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public TypeEntity getEntity(Long id) throws ObjectNotFoundException {

        return repository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Type", id));
    }

    public TypeEntity getEntity(String name) throws ObjectNotFoundException {

        TypeEntity entity = repository.findByName(name);
        if (Objects.nonNull(entity)) {
            return entity;
        }

        throw new ObjectNotFoundException("Type", name);
    }

    public Type getDto(Long id) throws ObjectNotFoundException {

        return typeMapper.toDto(getEntity(id));
    }
}

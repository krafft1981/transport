package com.rental.transport.service;

import com.rental.transport.entity.AbstractEntity;
import com.rental.transport.entity.AbstractRepository;
import com.rental.transport.dto.AbstractDto;
import com.rental.transport.mapper.AbstractMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

public abstract class AbstractService<
        E extends AbstractEntity,
        R extends AbstractRepository<E>,
        M extends AbstractMapper<E, D>,
        D extends AbstractDto> implements AbstractInterface<E, D> {

    protected final R repository;
    protected final M mapper;

    @Autowired
    public AbstractService(R repository, M mapper) {
        this.repository = repository;
        this.mapper     = mapper;
    }

    public Long create(@NonNull Principal principal) {

        E entity = mapper.create();
        return repository.save(entity).getId();
    }

    public List<D> getPage(@NonNull Pageable pageable) {

        return repository
                .findAll(pageable)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public List<D> getPage(@NonNull Principal principal, @NonNull Pageable pageable) {

        // filter
        return repository
                .findAll(pageable)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public D findById(@NonNull Long id) throws ObjectNotFoundException {

        E entity = repository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException(id));

        return mapper.toDto(entity);
    }

    public void update(@NonNull Principal principal, @NonNull D dto) throws ObjectNotFoundException, AccessDeniedException {

        // filter
        E entity = repository
                .findById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException(dto.getId()));

        entity = mapper.toEntity(dto);
        repository.save(entity);
    }

    public void delete(@NonNull Principal principal, @NonNull Long id) throws AccessDeniedException {
        // filter
        repository.deleteById(id);
    }

    public Long count() {
        return repository.count();
    }

    public Long count(@NonNull Principal principal) {
        return repository.count();
    }
}

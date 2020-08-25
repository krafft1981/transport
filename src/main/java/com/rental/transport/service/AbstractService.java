package com.rental.transport.service;

import com.rental.transport.dao.AbstractEntity;
import com.rental.transport.dao.AbstractRepository;
import com.rental.transport.dto.AbstractDto;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.security.Principal;
import java.util.List;
import lombok.NonNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

public abstract class AbstractService<
        E extends AbstractEntity,
        R extends AbstractRepository<E>,
        D extends AbstractDto> implements AbstractInterface<E, D> {

    protected final R repository;

    @Autowired
    public AbstractService(R repository) {
        this.repository = repository;
    }

    @Autowired
    private ModelMapper mapper;

    public Long create(@NonNull Principal principal) {

        D dto = null;
        E entity = null;// = mapper.map(dto, E);
        return repository.save(entity).getId();
    }

    public List<D> getPage(@NonNull Pageable pageable) {

        return null;
    }

    public List<D> getPage(@NonNull Principal principal, @NonNull Pageable pageable) {

        // filter

        List<E> entityes = repository.findAll(pageable).getContent();
        return null;
//        return entityes
//                .stream()
//                .map(entity -> { return mapper.map(entity, D ); } )
//                .collect(Collectors.toList());
    }

    public D findById(@NonNull Long id) throws ObjectNotFoundException {

        E entity = repository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("", id));
        return null;
//        return mapper.map(entity, D);
    }

    public void update(@NonNull Principal principal, @NonNull D dto) throws ObjectNotFoundException, AccessDeniedException {

        // filter

        E entity = repository
                .findById(dto.getId())
                .orElseThrow(() -> new ObjectNotFoundException("", dto.getId()));

//        entity = mapper.map(dto, E);
//        repository.save(entity);
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

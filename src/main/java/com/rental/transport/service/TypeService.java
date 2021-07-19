package com.rental.transport.service;

import com.rental.transport.dto.Type;
import com.rental.transport.entity.TransportTypeEntity;
import com.rental.transport.entity.TransportTypeRepository;
import com.rental.transport.mapper.TypeMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TypeService {

    @Autowired
    private TransportTypeRepository repository;

    @Autowired
    private TypeMapper typeMapper;

    public List<Type> getPage(Pageable pageable) {

        return repository
                .findAllByEnableTrue(pageable)
                .parallelStream()
                .filter(TransportTypeEntity::getEnable)
                .map(entity -> typeMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public TransportTypeEntity getEntity(Long id) throws ObjectNotFoundException {

        return repository
                .findById(id)
                .filter(TransportTypeEntity::getEnable)
                .orElseThrow(() -> new ObjectNotFoundException("Type", id));
    }

    public TransportTypeEntity getEntity(String name) throws ObjectNotFoundException {

        TransportTypeEntity entity = repository.findByEnableTrueAndName(name);
        if (Objects.nonNull(entity))
            return entity;

        throw new ObjectNotFoundException("Type", name);
    }

    public Type getDto(Long id) throws ObjectNotFoundException {

        return typeMapper.toDto(getEntity(id));
    }

    public Long create(String name) {

        try {
            return getEntity(name).getId();
        } catch (ObjectNotFoundException e) {
            TransportTypeEntity entity = new TransportTypeEntity(name);
            return repository.save(entity).getId();
        }
    }

    @PostConstruct
    public void postConstruct() {

        create("Яхта");
        create("Катамаран");
        create("Катер");
    }
}

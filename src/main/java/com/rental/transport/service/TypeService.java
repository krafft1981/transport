package com.rental.transport.service;

import com.rental.transport.dto.TransportTypeDto;
import com.rental.transport.entity.TransportTypeEntity;
import com.rental.transport.mapper.TransportTypeMapper;
import com.rental.transport.repository.TransportTypeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TypeService {

    private final TransportTypeRepository repository;

    @Autowired
    private TransportTypeMapper transportTypeMapper;

    public List<TransportTypeDto> getPage(Pageable pageable) {

        return repository
                .findAllByEnableTrue(pageable)
                .parallelStream()
                .filter(TransportTypeEntity::getEnable)
                .map(transportTypeMapper::entityToDto)
                .collect(Collectors.toList());
    }

    public TransportTypeEntity getEntity(UUID id) {

        final var entity = repository.getReferenceById(id);
        if (entity.getEnable()) {
            return entity;
        }
        throw new EntityNotFoundException();
    }

    public TransportTypeEntity getEntity(String name) {

        TransportTypeEntity entity = repository.findByEnableTrueAndName(name);
        if (Objects.isNull(entity))
            throw new EntityNotFoundException();

        return entity;
    }

    public TransportTypeDto getDto(UUID id) {
        return transportTypeMapper.entityToDto(getEntity(id));
    }

    public UUID create(String name) {

        try {
            return getEntity(name).getId();
        } catch (EntityNotFoundException e) {
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

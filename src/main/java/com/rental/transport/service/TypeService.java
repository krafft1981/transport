package com.rental.transport.service;

import com.rental.transport.dto.TransportTypeDto;
import com.rental.transport.entity.TransportTypeEntity;
import com.rental.transport.mapper.TransportTypeMapper;
import com.rental.transport.repository.TransportTypeRepository;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import jakarta.annotation.PostConstruct;
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
                   .map(entity -> transportTypeMapper.toDto(entity))
                   .collect(Collectors.toList());
    }

    public TransportTypeEntity getEntity(UUID id) throws ObjectNotFoundException {

        return repository
                   .findById(id)
                   .filter(TransportTypeEntity::getEnable)
                   .orElseThrow(() -> new ObjectNotFoundException("TransportType", id));
    }

    public TransportTypeEntity getEntity(String name) throws ObjectNotFoundException {

        TransportTypeEntity entity = repository.findByEnableTrueAndName(name);
        if (Objects.isNull(entity))
            throw new ObjectNotFoundException("TransportType", name);

        return entity;
    }

    public TransportTypeDto getDto(UUID id) throws ObjectNotFoundException {

//        return typeMapper.toDto(getEntity(id));
        return new TransportTypeDto();
    }

    public UUID create(String name) {

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
    };
}

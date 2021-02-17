package com.rental.transport.service;

import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.TemplatesRepository;
import com.rental.transport.entity.TypeEntity;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplatesService {

    @Autowired
    private TemplatesRepository templatesRepository;

    public Set<PropertyEntity> getTypeProperty(TypeEntity type) {

        return StreamSupport
                .stream(templatesRepository
                        .findByTransportTypeId(type.getId())
                        .spliterator(), false)
                .map(entity -> {
                    return new PropertyEntity(entity.getPropertyTypeEntity(), entity.getValue());
                })
                .collect(Collectors.toSet());
    }

    public Long count() {
        return templatesRepository.count();
    }
}

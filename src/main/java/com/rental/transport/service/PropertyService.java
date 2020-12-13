package com.rental.transport.service;

import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyRepository;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository repository;

    private PropertyEntity searchProperty(Set<PropertyEntity> entryes, String name) throws ObjectNotFoundException {

        return entryes
                .stream()
                .filter(property -> property.getLogicName().equals(name))
                .findFirst()
                .orElseThrow(() -> new ObjectNotFoundException("Property", name));
    }

    public String getValue(Set<PropertyEntity> entryes, String name) throws ObjectNotFoundException {

        PropertyEntity entity = searchProperty(entryes, name);
        return entity.getValue();
    }

    public void setValue(Set<PropertyEntity> entryes, String name, String value) throws ObjectNotFoundException {

        PropertyEntity entity = searchProperty(entryes, name);
        entity.setValue(value);
        repository.save(entity);
    }
}

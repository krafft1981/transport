package com.rental.transport.service;

import com.rental.transport.dto.Property;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyRepository;
import com.rental.transport.entity.PropertyTypeEntity;
import com.rental.transport.entity.PropertyTypeRepository;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.PropertyMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PropertyService {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    @Autowired
    private PropertyMapper propertyMapper;

    public PropertyEntity searchProperty(Set<PropertyEntity> entryes, String name) throws ObjectNotFoundException {

        return entryes
                .stream()
                .filter(property -> property.getType().getLogicName().equals(name))
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
        propertyRepository.save(entity);
    }

    public List<Property> getPage(Pageable pageable) {

        return propertyRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> propertyMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public PropertyEntity create(String name, String value) throws ObjectNotFoundException {

        PropertyTypeEntity type = propertyTypeRepository.findByLogicName(name);
        if (Objects.isNull(type))
            throw new ObjectNotFoundException("Property Type", name);

        PropertyEntity property = new PropertyEntity(type, value);
        propertyRepository.save(property);
        return property;
    }

    public void createType(String logic, String name, PropertyTypeEnum valueType) {

        PropertyTypeEntity type = propertyTypeRepository.findByLogicName(logic);
        if (Objects.isNull(type)) {
            type = new PropertyTypeEntity(logic, name, valueType);
            propertyTypeRepository.save(type);
        }
    }

    public PropertyEntity copy(String newType, Set<PropertyEntity> entryes, String oldType)
            throws ObjectNotFoundException {

        PropertyTypeEntity pNew = propertyTypeRepository.findByLogicName(newType);
        if (Objects.isNull(pNew))
            throw new ObjectNotFoundException("PropertyType", newType);

        PropertyTypeEntity pOld = propertyTypeRepository.findByLogicName(oldType);
        if (Objects.isNull(pOld))
            throw new ObjectNotFoundException("PropertyType", oldType);

        PropertyEntity entity = searchProperty(entryes, oldType);
        return create(newType, entity.getValue());
    }
}

package com.rental.transport.service;

import com.rental.transport.dto.Property;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyRepository;
import com.rental.transport.entity.PropertyTypeEntity;
import com.rental.transport.entity.PropertyTypeRepository;
import com.rental.transport.enums.PropertyNameEnum;
import com.rental.transport.mapper.PropertyMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyTypeRepository propertyTypeRepository;
    private final PropertyMapper propertyMapper;

    public PropertyEntity getProperty(Set<PropertyEntity> entries, PropertyNameEnum propertyName)
        throws ObjectNotFoundException {

        return entries
                   .parallelStream()
                   .filter(property -> property.getType().getLogicName().equals(propertyName.getLogicName()))
                   .findFirst()
                   .orElseThrow(() -> new ObjectNotFoundException("Property", propertyName.getLogicName()));
    }

    public PropertyTypeEntity getPropertyType(PropertyNameEnum property)
        throws ObjectNotFoundException {

        PropertyTypeEntity type = propertyTypeRepository.findByLogicName(property.getLogicName());
        if (Objects.isNull(type)) {
            type = propertyTypeRepository.save(new PropertyTypeEntity(property));
        }

        return type;
    }

    public String getValue(Set<PropertyEntity> entries, PropertyNameEnum property) throws ObjectNotFoundException {

        return getProperty(entries, property).getValue();
    }

    public void setValue(Set<PropertyEntity> entries, PropertyNameEnum propertyName, String value)
        throws ObjectNotFoundException {

        PropertyEntity entity = getProperty(entries, propertyName);
        entity.setValue(value);
        propertyRepository.save(entity);
    }

    public List<Property> getPage(Pageable pageable) {

        return propertyRepository
                   .findAll(pageable)
                   .getContent()
                   .parallelStream()
                   .map(propertyMapper::toDto)
                   .collect(Collectors.toList());
    }

    public PropertyEntity create(PropertyNameEnum property, String value) throws ObjectNotFoundException {

        PropertyTypeEntity type = getPropertyType(property);
        PropertyEntity entity = new PropertyEntity(type, value);
        return propertyRepository.save(entity);
    }

    public PropertyEntity copy(PropertyNameEnum newType, Set<PropertyEntity> entryes, PropertyNameEnum oldType)
        throws ObjectNotFoundException {

        getPropertyType(newType);
        getPropertyType(oldType);

        PropertyEntity entity = getProperty(entryes, oldType);
        return create(newType, entity.getValue());
    }
}

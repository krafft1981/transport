package com.rental.transport.mapper;

import com.rental.transport.dto.PropertyDto;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.repository.PropertyRepository;
import org.hibernate.ObjectDeletedException;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

@Mapper(componentModel = "spring")
public abstract class PropertyMapper {

    @Autowired
    private PropertyRepository propertyRepository;

    public abstract PropertyEntity dtoToEntity(PropertyDto dto);

    public abstract PropertyDto entityToDto(PropertyEntity entity);

    public PropertyEntity uuidToEntity(UUID id) throws ObjectDeletedException {
        return propertyRepository.getReferenceById(id);
    }

    public UUID entityToUuid(PropertyEntity entity) {
        return entity.getId();
    }
}

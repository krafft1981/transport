package com.rental.transport.mapper;

import com.rental.transport.dto.Property;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyRepository;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PropertyMapper implements AbstractMapper<PropertyEntity, Property> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private PropertyRepository repository;

    @Override
    public PropertyEntity toEntity(Property dto) {
        System.out.println("toEntity: " + dto.toString());
        return Objects.isNull(dto) ? null : mapper.map(dto, PropertyEntity.class);
    }

    @Override
    public Property toDto(PropertyEntity entity) {
        System.out.println("toDto: " + entity.toString());
        return Objects.isNull(entity) ? null : mapper.map(entity, Property.class);
    }

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(PropertyEntity.class, Property.class)
                .addMappings(m -> m.skip(Property::setName))
                .addMappings(m -> m.skip(Property::setValue))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Property.class, PropertyEntity.class)
                .addMappings(m -> m.skip(PropertyEntity::setName))
                .addMappings(m -> m.skip(PropertyEntity::setValue))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(PropertyEntity source, Property destination) {

        destination.setId(source.getId());
        destination.setName(source.getName());
        destination.setValue(source.getValue());
    }

    public void mapSpecificFields(Property source, PropertyEntity destination) {

        PropertyEntity entity = null;

        if (Objects.isNull(source.getId())) {
            entity = new PropertyEntity(source.getName(), source.getValue());
            repository.save(entity);
        }
        else {
            entity = repository
                    .findById(source.getId())
                    .orElse(null);
        }

        if (Objects.isNull(entity)) {
            entity = new PropertyEntity(source.getName(), source.getValue());
            repository.save(entity);
        }
        else {
            entity.setName(source.getName());
            entity.setValue(source.getValue());
        }

        destination.setId(entity.getId());
        destination.setName(source.getName());
        destination.setValue(source.getValue());
    }
}

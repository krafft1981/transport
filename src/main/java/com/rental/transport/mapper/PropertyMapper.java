package com.rental.transport.mapper;

import com.rental.transport.dto.Property;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyTypeEntity;
import com.rental.transport.entity.PropertyTypeRepository;
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
    private PropertyTypeRepository propertyTypeRepository;

    @Override
    public PropertyEntity toEntity(Property dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, PropertyEntity.class);
    }

    @Override
    public Property toDto(PropertyEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Property.class);
    }

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(PropertyEntity.class, Property.class)
                .addMappings(m -> m.skip(Property::setId))
                .addMappings(m -> m.skip(Property::setHumanName))
                .addMappings(m -> m.skip(Property::setLogicName))
                .addMappings(m -> m.skip(Property::setType))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Property.class, PropertyEntity.class)
                .addMappings(m -> m.skip(PropertyEntity::setId))
                .addMappings(m -> m.skip(PropertyEntity::setType))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(PropertyEntity source, Property destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());

        destination.setHumanName(source.getType().getHumanName());
        destination.setLogicName(source.getType().getLogicName());
        destination.setType(source.getType().getType().name());
    }

    public void mapSpecificFields(Property source, PropertyEntity destination) {

        destination.setId(source.getId());

        PropertyTypeEntity type = propertyTypeRepository.findByLogicName(source.getLogicName());
        if (!Objects.isNull(type)) {
            destination.setType(type);
        }
    }
}

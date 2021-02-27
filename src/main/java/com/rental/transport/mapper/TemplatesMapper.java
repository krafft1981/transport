package com.rental.transport.mapper;

import com.rental.transport.dto.Property;
import com.rental.transport.dto.PropertyType;
import com.rental.transport.dto.Templates;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyTypeEntity;
import com.rental.transport.entity.TemplatesEntity;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TemplatesMapper implements AbstractMapper<TemplatesEntity, Templates> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public TemplatesEntity toEntity(Templates dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, TemplatesEntity.class);
    }

    @Override
    public Templates toDto(TemplatesEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Templates.class);
    }


    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(TemplatesEntity.class, Templates.class)
                .addMappings(m -> m.skip(Templates::setPropertyType))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Templates.class, TemplatesEntity.class)
                .addMappings(m -> m.skip(TemplatesEntity::setPropertyTypeEntity))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(TemplatesEntity source, Templates destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());
        destination.setPropertyType(mapper.map(source.getPropertyTypeEntity(), PropertyType.class));
    }

    public void mapSpecificFields(Templates source, TemplatesEntity destination) {

        destination.setId(source.getId());
        destination.setPropertyTypeEntity(mapper.map(source.getPropertyType(), PropertyTypeEntity.class));
    }
}

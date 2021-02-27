package com.rental.transport.service;

import com.rental.transport.dto.PropertyType;
import com.rental.transport.dto.Templates;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyTypeEntity;
import com.rental.transport.entity.PropertyTypeRepository;
import com.rental.transport.entity.TemplatesEntity;
import com.rental.transport.entity.TemplatesRepository;
import com.rental.transport.entity.TypeEntity;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.PropertyTypeMapper;
import com.rental.transport.mapper.TemplatesMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.validator.ValidatorFactory;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TemplatesService {

    @Autowired
    private TemplatesRepository templatesRepository;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    @Autowired
    private TemplatesMapper templatesMapper;

    @Autowired
    private PropertyTypeMapper propertyTypeMapper;

    @Autowired
    private TypeService typeService;

    private ValidatorFactory vFactory = new ValidatorFactory();

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

    public TemplatesEntity getEntity(Long id) throws ObjectNotFoundException {

        return templatesRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Templates", id));
    }

    public Templates getDto(Long id) throws ObjectNotFoundException {

        return templatesMapper.toDto(getEntity(id));
    }

    public void delete(Long id) {
        templatesRepository.deleteById(id);
    }

    public void update(Templates templates) {
        TemplatesEntity entity = templatesMapper.toEntity(templates);
        templatesRepository.save(entity);
    }

    public Long create(String type, String logicName, String defValue) throws ObjectNotFoundException {

        TypeEntity typeEntity = typeService.getEntity(type);
        PropertyTypeEntity propertyTypeEntity = propertyTypeRepository.findByLogicName(logicName);

        if (propertyTypeEntity == null)
            throw new ObjectNotFoundException("logicName", logicName);

        TemplatesEntity templatesEntity = new TemplatesEntity(typeEntity, propertyTypeEntity, defValue);
        return templatesRepository.save(templatesEntity).getId();
    }

    public List<Templates> getTemplatesPage(Pageable pageable) {

        return templatesRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> {
                    return templatesMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public Long nameCount() {
        return propertyTypeRepository.count();
    }

    public List<PropertyType> getPropertyTypePage(Pageable pageable) {

        return propertyTypeRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> {
                    return propertyTypeMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public PropertyTypeEntity getPropertyTypeEntity(Long id) throws ObjectNotFoundException {

        return propertyTypeRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("PropertyType", id));
    }

    public PropertyType getPropertyTypeDto(Long id) throws ObjectNotFoundException {

        return propertyTypeMapper.toDto(getPropertyTypeEntity(id));
    }

    public void deletePropertyType(Long id) {
        propertyTypeRepository.deleteById(id);
    }

    public void updatePropertyType(PropertyType propertyType) {

        PropertyTypeEntity entity = propertyTypeMapper.toEntity(propertyType);
        propertyTypeRepository.save(entity);
    }

    public Long createPropertyType(String type, String logicName, String humanName) throws IllegalArgumentException {

        vFactory.getValidator(type);
        PropertyTypeEntity propertyTypeEntity = new PropertyTypeEntity(
                humanName,
                logicName,
                PropertyTypeEnum.valueOf(type)
        );

        return propertyTypeRepository.save(propertyTypeEntity).getId();
    }
}

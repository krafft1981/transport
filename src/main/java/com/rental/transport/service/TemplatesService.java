package com.rental.transport.service;

import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyTypeEntity;
import com.rental.transport.entity.PropertyTypeRepository;
import com.rental.transport.entity.TemplatesEntity;
import com.rental.transport.entity.TemplatesRepository;
import com.rental.transport.entity.TransportTypeEntity;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.ValidatorFactory;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TemplatesService {

    @Autowired
    private TemplatesRepository templatesRepository;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private TypeService typeService;

    private ValidatorFactory vFactory = new ValidatorFactory();

    public Set<PropertyEntity> getTypeProperty(TransportTypeEntity type) {

        return StreamSupport
                .stream(templatesRepository
                        .findByTransportTypeId(type.getId())
                        .spliterator(), false)
                .map(entity -> {
                    return new PropertyEntity(entity.getPropertyType(), entity.getValue());
                })
                .collect(Collectors.toSet());
    }

    public TemplatesEntity getEntity(Long id) throws ObjectNotFoundException {

        return templatesRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Templates", id));
    }

    public void create(String type, String logicName, String defValue) throws ObjectNotFoundException {

        TransportTypeEntity transportTypeEntity = typeService.getEntity(type);
        PropertyTypeEntity propertyTypeEntity = propertyTypeRepository.findByLogicName(logicName);

        if (Objects.isNull(propertyTypeEntity))
            throw new ObjectNotFoundException("logicName", logicName);

        if (templatesRepository.findByTransportTypeIdAndPropertyTypeId(transportTypeEntity.getId(), propertyTypeEntity.getId()).isEmpty()) {
            TemplatesEntity templatesEntity = new TemplatesEntity(transportTypeEntity, propertyTypeEntity, defValue);
            templatesRepository.save(templatesEntity);
        }
    }

    @PostConstruct
    public void postConstruct() {

        propertyService.createType("transport_name", "Название", PropertyTypeEnum.String);
        propertyService.createType("transport_capacity", "Количество гостей", PropertyTypeEnum.Integer);
        propertyService.createType("transport_price", "Цена", PropertyTypeEnum.Double);
        propertyService.createType("transport_min_rent_time", "Минимальное время аренды", PropertyTypeEnum.Hour);
        propertyService.createType("transport_use_driver", "Сдаётся с водителем", PropertyTypeEnum.Boolean);
        propertyService.createType("transport_description", "Описание", PropertyTypeEnum.String);

        create("Яхта", "transport_name", "Не указано");
        create("Яхта", "transport_capacity", "1");
        create("Яхта", "transport_price", "1000");
        create("Яхта", "transport_min_rent_time", "1");
        create("Яхта", "transport_description", "Не указано");
        create("Яхта", "transport_use_driver", "Да");

        create("Катамаран", "transport_name", "Не указано");
        create("Катамаран", "transport_capacity", "1");
        create("Катамаран", "transport_price", "1000");
        create("Катамаран", "transport_description", "Не указано");
        create("Катамаран", "transport_min_rent_time", "1");
        create("Катамаран", "transport_use_driver", "Да");

        create("Байдарка", "transport_name", "Не указано");
        create("Байдарка", "transport_capacity", "3");
        create("Байдарка", "transport_price", "150");
        create("Байдарка", "transport_description", "Не указано");
        create("Байдарка", "transport_min_rent_time", "3");
        create("Байдарка", "transport_use_driver", "Нет");
    }
}

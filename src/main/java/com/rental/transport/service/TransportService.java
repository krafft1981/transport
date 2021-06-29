package com.rental.transport.service;

import com.rental.transport.dto.Property;
import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.entity.TransportTypeEntity;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.TransportMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.ValidatorFactory;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransportService {

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private ImageService imageService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private ParkingService parkingService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private TransportMapper transportMapper;

    private ValidatorFactory vf = new ValidatorFactory();

    public void delete(String account, Long id)
            throws AccessDeniedException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = getEntity(id);

        if (transport.getCustomer().contains(customer) == false)
            throw new AccessDeniedException("Delete");

        transport.getProperty().clear();
        transport.getImage().clear();
        transport.setType(null);
        transportRepository.delete(transport);
    }

    public Long create(String account, Long typeId)
            throws ObjectNotFoundException {

        CustomerEntity customerEntity = customerService.getEntity(account);
        TransportTypeEntity transportTypeEntity = typeService.getEntity(typeId);
        TransportEntity transport = new TransportEntity(customerEntity, transportTypeEntity);
        transport.addProperty(
                propertyService.create("transport_name", "Не указано"),
                propertyService.create("transport_capacity", "1"),
                propertyService.create("transport_price", "1000"),
                propertyService.create("transport_min_rent_time", "2"),
                propertyService.create("transport_description", "Не указано")
        );

        return transportRepository.save(transport).getId();
    }

    public Transport update(String account, Transport transport)
            throws AccessDeniedException, ObjectNotFoundException, IllegalArgumentException {

        for (Property property : transport.getProperty()) {
            if (!vf.getValidator(property.getType()).validate(property.getValue()))
                throw new IllegalArgumentException("Неправильное значение поля: '" + property.getHumanName() + "'");
        }

        TransportEntity entity = transportMapper.toEntity(transport);
        CustomerEntity customer = customerService.getEntity(account);

        if (!entity.getCustomer().contains(customer))
            throw new AccessDeniedException("Change");

        transportRepository.save(entity);
        return transportMapper.toDto(entity);
    }

    public List<Transport> getPage(Pageable pageable) {

        return transportRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .filter(entity -> entity.getEnable())
                .filter(entity -> entity.getType().getEnable())
                .map(entity -> transportMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Transport> getPageTyped(Pageable pageable, Long type) {

        return transportRepository
                .findAllByEnableTrueAndTypeId(pageable, type)
                .stream()
                .filter(entity -> entity.getType().getEnable())
                .map(entity -> transportMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Transport> getMyTransport(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return transportRepository.findAllByCustomerIdAndEnableTrue(customer.getId())
                .stream()
                .map(entity -> transportMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public TransportEntity getEntity(Long id) throws ObjectNotFoundException {

        return transportRepository
                .findById(id)
                .filter(entity -> entity.getEnable())
                .filter(entity -> entity.getType().getEnable())
                .orElseThrow(() -> new ObjectNotFoundException("Transport", id));
    }

    public Transport getDto(Long id) throws ObjectNotFoundException {

        TransportEntity entity = getEntity(id);
        return transportMapper.toDto(entity);
    }

    public List<Transport> getParkingTransport(Long parkingId) throws ObjectNotFoundException {

        return parkingService.getEntity(parkingId)
                .getTransport()
                .stream()
                .filter(entity -> entity.getEnable())
                .filter(entity -> entity.getType().getEnable())
                .map(transport -> transportMapper.toDto(transport))
                .collect(Collectors.toList());
    }

    @Transactional
    public Transport addTransportImage(String account, Long transportId, byte[] data)
            throws AccessDeniedException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = getEntity(transportId);
        ImageEntity image = new ImageEntity(data);
        transport.addImage(image);
        transportRepository.save(transport);
        return transportMapper.toDto(transport);
    }

    @Transactional
    public Transport delTransportImage(String account, Long transportId, Long imageId)
            throws AccessDeniedException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = getEntity(transportId);
        transport.delImage(imageService.getEntity(imageId));
        transportRepository.save(transport);
        return transportMapper.toDto(transport);
    }

    @PostConstruct
    public void postConstruct() {

        propertyService.createType("transport_name", "Название", PropertyTypeEnum.String);
        propertyService.createType("transport_capacity", "Максимальное количество гостей", PropertyTypeEnum.Integer);
        propertyService.createType("transport_price", "Цена за час", PropertyTypeEnum.Double);
        propertyService.createType("transport_min_rent_time", "Минимальное время аренды(часов)", PropertyTypeEnum.Hour);
        propertyService.createType("transport_description", "Описание", PropertyTypeEnum.String);
    }
}

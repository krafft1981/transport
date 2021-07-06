package com.rental.transport.mapper;

import com.rental.transport.dto.Customer;
import com.rental.transport.dto.Property;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.ImageRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.ParkingRepository;
import com.rental.transport.entity.PropertyRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper implements AbstractMapper<CustomerEntity, Customer> {

    @Autowired
    private PropertyRepository propertyRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private ParkingRepository parkingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private ModelMapper mapper;

    public CustomerEntity toEntity(Customer dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, CustomerEntity.class);
    }

    public Customer toDto(CustomerEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Customer.class);
    }

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(CustomerEntity.class, Customer.class)
                .addMappings(m -> m.skip(Customer::setId))
                .addMappings(m -> m.skip(Customer::setTransport))
                .addMappings(m -> m.skip(Customer::setParking))
                .addMappings(m -> m.skip(Customer::setImage))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Customer.class, CustomerEntity.class)
                .addMappings(m -> m.skip(CustomerEntity::setId))
                .addMappings(m -> m.skip(CustomerEntity::setTransport))
                .addMappings(m -> m.skip(CustomerEntity::setParking))
                .addMappings(m -> m.skip(CustomerEntity::setImage))
                .addMappings(m -> m.skip(CustomerEntity::setProperty))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(CustomerEntity source, Customer destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());

        source.getTransport().forEach(transport -> destination.addTransport(transport.getId()));
        source.getParking().forEach(parking -> destination.addParking(parking.getId()));
        source.getImage().forEach(image -> destination.addImage(image.getId()));
    }

    public void mapSpecificFields(Customer source, CustomerEntity destination) {

        destination.setId(source.getId());

        source
                .getTransport()
                .forEach(id -> {
                    TransportEntity transport = transportRepository.findById(id).orElse(null);
                    if (Objects.nonNull(transport)) {
                        destination.addTransport(transport);
                    }
                });

        source
                .getParking()
                .forEach(id -> {
                    ParkingEntity parking = parkingRepository.findById(id).orElse(null);
                    if (Objects.nonNull(parking)) {
                        destination.addParking(parking);
                    }
                });

        source
                .getImage()
                .forEach(id -> {
                    ImageEntity image = imageRepository.findById(id).orElse(null);
                    if (Objects.nonNull(image)) {
                        destination.addImage(image);
                    }
                });

        CustomerEntity customer = customerRepository.findById(source.getId()).orElse(null);
        if (Objects.nonNull(customer)) {
            customer
                    .getProperty()
                    .forEach(entity -> {
                        Property property = source.getProperty().stream()
                                .filter(it -> it.getLogicName().equals(entity.getType().getLogicName()))
                                .findFirst()
                                .orElse(null);

                        if (Objects.nonNull(property)) {
                            entity.setValue(property.getValue());
                        }

                        destination.addProperty(entity);
                    });
        }
    }
}

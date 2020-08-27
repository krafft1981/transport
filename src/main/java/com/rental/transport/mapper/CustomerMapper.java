package com.rental.transport.mapper;

import com.rental.transport.dto.Customer;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper implements AbstractMapper<CustomerEntity, Customer> {

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private ModelMapper mapper;

    public CustomerEntity toEntity(Customer dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, CustomerEntity.class);
    }

    public Customer toDto(CustomerEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Customer.class);
    }

    @Override
    public CustomerEntity create() {
        CustomerEntity entity = new CustomerEntity();
        return entity;
    }


    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(CustomerEntity.class, Customer.class)
                .addMappings(m -> m.skip(Customer::setId))
                .addMappings(m -> m.skip(Customer::setTransport))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Customer.class, CustomerEntity.class)
                .addMappings(m -> m.skip(CustomerEntity::setId))
                .addMappings(m -> m.skip(CustomerEntity::setTransport))
                .setPostConverter(toEntityConverter());
    }

    private Converter<CustomerEntity, Customer> toDtoConverter() {
        return context -> {
            CustomerEntity source = context.getSource();
            Customer destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private Converter<Customer, CustomerEntity> toEntityConverter() {
        return context -> {
            Customer source = context.getSource();
            CustomerEntity destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(CustomerEntity source, Customer destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());
        source
                .getTransport()
                .stream()
                .forEach(transport -> { destination.addTransport(transport.getId()); });
    }

    private void mapSpecificFields(Customer source, CustomerEntity destination) {

        destination.setId(source.getId());
        source
                .getTransport()
                .stream()
                .forEach(id -> {
                    TransportEntity transport = transportRepository.findById(id).orElse(null);
                    if (transport != null) {
                        destination.addTransport(transport);
                    }
                });
    }
}

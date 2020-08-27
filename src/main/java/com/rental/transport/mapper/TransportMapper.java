package com.rental.transport.mapper;

import com.rental.transport.dto.Transport;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.TransportEntity;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransportMapper implements AbstractMapper<TransportEntity, Transport> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public TransportEntity toEntity(Transport dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, TransportEntity.class);
    }

    @Override
    public Transport toDto(TransportEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Transport.class);
    }

    @Override
    public TransportEntity create() {
        TransportEntity entity = new TransportEntity();
        return entity;
    }

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(TransportEntity.class, Transport.class)
                .addMappings(m -> m.skip(Transport::setId))
                .addMappings(m -> m.skip(Transport::setCustomer))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Transport.class, TransportEntity.class)
                .addMappings(m -> m.skip(TransportEntity::setId))
                .addMappings(m -> m.skip(TransportEntity::setCustomer))
                .setPostConverter(toEntityConverter());
    }

    private Converter<TransportEntity, Transport> toDtoConverter() {
        return context -> {
            TransportEntity source = context.getSource();
            Transport destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private Converter<Transport, TransportEntity> toEntityConverter() {
        return context -> {
            Transport source = context.getSource();
            TransportEntity destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private void mapSpecificFields(TransportEntity source, Transport destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());
        source
                .getCustomer()
                .stream()
                .forEach(customer -> { destination.addCustomer(customer.getId()); });
    }

    private void mapSpecificFields(Transport source, TransportEntity destination) {

        destination.setId(source.getId());
        source
                .getCustomer()
                .stream()
                .forEach(id -> {
                    CustomerEntity customer = customerRepository.findById(id).orElse(null);
                    if (customer != null) {
                        destination.addCustomer(customer);
                    }
                });
    }
}

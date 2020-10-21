package com.rental.transport.mapper;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.ImageEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import java.util.Date;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements AbstractMapper<OrderEntity, Order> {

    @Autowired
    private ModelMapper mapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private TransportMapper transportMapper;

    public OrderEntity toEntity(Order dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, OrderEntity.class);
    }

    public Order toDto(OrderEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Order.class);
    }
/*
    @PostConstruct
    public void postConstruct() {

        mapper.createTypeMap(OrderEntity.class, Order.class)
                .addMappings(m -> m.skip(Order::setId))
                .addMappings(m -> m.skip(Order::setStartAt))
                .addMappings(m -> m.skip(Order::setStopAt))
                .addMappings(m -> m.skip(Order::setCreatedAt))
                .addMappings(m -> m.skip(Order::setCustomer))
                .addMappings(m -> m.skip(Order::setTransport))
                  .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Order.class, OrderEntity.class)
                .addMappings(m -> m.skip(OrderEntity::setId))
                .addMappings(m -> m.skip(OrderEntity::setStartAt))
                .addMappings(m -> m.skip(OrderEntity::setStopAt))
                .addMappings(m -> m.skip(OrderEntity::setCreatedAt))
                .addMappings(m -> m.skip(OrderEntity::setCustomer))
                .addMappings(m -> m.skip(OrderEntity::setTransport))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(OrderEntity source, Order destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());

        if (Objects.nonNull(source.getStartAt())) {
            Long value = source.getStartAt().getTime() / 1000;
            destination.setStartAt(value.intValue());
        }

        if (Objects.nonNull(source.getStopAt())) {
            Long value = source.getStopAt().getTime() / 1000;
            destination.setStopAt(value.intValue());
        }

        if (Objects.nonNull(source.getCreatedAt())) {
            Long value = source.getCreatedAt().getTime() / 1000;
            destination.setCreatedAt(value.intValue());
        }

        destination.setCustomer(source.getCustomer().getId());
        destination.setTransport(transportMapper.toDto(source.getTransport()));
    }

    public void mapSpecificFields(Order source, OrderEntity destination) {

        destination.setId(source.getId());

        destination.setStartAt(new Date((long) source.getStartAt() * 1000));
        destination.setStopAt(new Date((long) source.getStopAt() * 1000));
        destination.setCreatedAt(new Date((long) source.getCreatedAt() * 1000));

        CustomerEntity customer = customerRepository.findById(source.getCustomer()).orElse(null);
        if (Objects.nonNull(customer)) {
            destination.setCustomer(customer);
        }

        destination.setTransport(transportMapper.toEntity(source.getTransport()));
    }
*/
}

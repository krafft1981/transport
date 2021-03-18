package com.rental.transport.mapper;

import com.rental.transport.dto.Order;
import com.rental.transport.dto.Property;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.TransportRepository;
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
    private OrderRepository orderRepository;

    public OrderEntity toEntity(Order dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, OrderEntity.class);
    }

    public Order toDto(OrderEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Order.class);
    }

    @PostConstruct
    public void postConstruct() {

        mapper.createTypeMap(OrderEntity.class, Order.class)
                .addMappings(m -> m.skip(Order::setId))
                  .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Order.class, OrderEntity.class)
                .addMappings(m -> m.skip(OrderEntity::setId))
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(OrderEntity source, Order destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());
    }

    public void mapSpecificFields(Order source, OrderEntity destination) {

        destination.setId(source.getId());

//        OrderEntity order = orderRepository.findById(source.getId()).orElse(null);
//        if (Objects.nonNull(order)) {
//            order.getProperty().stream()
//                    .forEach( entity -> {
//                        Property property = source.getProperty().stream()
//                                .filter(it -> it.getLogicName().equals(entity.getType().getLogicName()))
//                                .findFirst()
//                                .orElse(null);
//
//                        if (Objects.nonNull(property)) {
//                            entity.setValue(property.getValue());
//                        }
//
//                        destination.addProperty(entity);
//                    });
//        }
    }
}

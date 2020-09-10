package com.rental.transport.mapper;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.OrderEntity;
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
    private CustomerMapper customerMapper;

    @Autowired
    private TransportMapper transportMapper;

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

        System.out.println(source + " " + destination);
    }

    public void mapSpecificFields(Order source, OrderEntity destination) {

        destination.setId(source.getId());

        System.out.println(source + " " + destination);
    }
}

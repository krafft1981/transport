package com.rental.transport.mapper;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.OrderEntity;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper implements AbstractMapper<OrderEntity, Order> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public OrderEntity toEntity(Order dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, OrderEntity.class);
    }

    @Override
    public Order toDto(OrderEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Order.class);
    }
}

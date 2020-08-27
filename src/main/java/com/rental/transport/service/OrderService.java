package com.rental.transport.service;

import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.dto.Order;
import com.rental.transport.mapper.OrderMapper;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends AbstractService<OrderEntity, OrderRepository, OrderMapper, Order> {

    public OrderService(OrderRepository repository, OrderMapper mapper) {
        super(repository, mapper);
    }
}

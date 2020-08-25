package com.rental.transport.service;

import com.rental.transport.dao.OrderEntity;
import com.rental.transport.dao.OrderRepository;
import com.rental.transport.dto.Order;
import org.springframework.stereotype.Service;

@Service
public class OrderService extends AbstractService<OrderEntity, OrderRepository, Order> {

    public OrderService(OrderRepository repository) {
        super(repository);
    }
}

package com.rental.transport.controller;

import com.rental.transport.dao.OrderEntity;
import com.rental.transport.dao.OrderRepository;
import com.rental.transport.dto.Order;
import com.rental.transport.service.OrderService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/order")
@RestController
public class OrderController extends AbstractController<OrderEntity, Order, OrderRepository, OrderService> {

    public OrderController(OrderService service) {
        super(service);
    }
}

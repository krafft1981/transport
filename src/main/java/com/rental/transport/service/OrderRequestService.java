package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderRequestEntity;
import com.rental.transport.entity.OrderRequestRepository;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderRequestService {

    @Autowired
    private OrderRequestRepository orderRequestRepository;

    @Autowired
    private PropertyService propertyService;

    public void createOrderBundle(TransportEntity transport, OrderEntity order) {

        transport
                .getCustomer()
                .stream()
                .forEach(id -> { createOrderBranch(id, order); });
    }

    public void createOrderBranch(CustomerEntity customer, OrderEntity order)
            throws IllegalArgumentException {

        OrderRequestEntity orderRequest = new OrderRequestEntity(customer, order);
        orderRequestRepository.save(orderRequest);
    }

    public void deleteOrderBranch(OrderEntity order, CustomerEntity driver) {
        orderRequestRepository.deleteByOrderIdAndCustomerId(order.getId(), driver.getId());
    }

    public void deleteOrderBundle(OrderEntity order) {

        orderRequestRepository.deleteByOrderId(order.getId());
    }

    public List<Long> getCustomerBranches(CustomerEntity customer) {
        return orderRequestRepository
                .findByCustomerId(customer.getId())
                .stream()
                .map(entity -> { return entity.getOrder().getId(); })
                .collect(Collectors.toList());
    }

    public void checkOrderRequestQuorum(OrderEntity order, TransportEntity transport)
            throws IllegalArgumentException, ObjectNotFoundException {

        String quorum = propertyService.getValue(transport.getProperty(), "quorum");
        if (orderRequestRepository.countByOrderId(order.getId()) < Long.getLong(quorum))
            throw new IllegalArgumentException("Customer quorum not reached");
    }
}

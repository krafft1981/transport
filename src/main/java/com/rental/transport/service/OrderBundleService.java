package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderBundleEntity;
import com.rental.transport.entity.OrderBundleRepository;
import com.rental.transport.entity.TransportEntity;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderBundleService {

    @Autowired
    private OrderBundleRepository orderBundleRepository;

    public void createOrderBundle(List<CustomerEntity> customers, OrderEntity order) {

        customers.stream().forEach(id -> { createOrderBranch(id, order); });
    }

    public void createOrderBranch(CustomerEntity customer, OrderEntity order)
            throws IllegalArgumentException {

        OrderBundleEntity orderRequest = new OrderBundleEntity(customer, order);
        orderBundleRepository.save(orderRequest);
    }

    public void deleteOrderBranch(OrderEntity order, CustomerEntity driver) {
        orderBundleRepository.deleteByOrderIdAndCustomerId(order.getId(), driver.getId());
    }

    public void deleteOrderBundle(OrderEntity order) {

        orderBundleRepository.deleteByOrderId(order.getId());
    }

    public List<Long> getCustomerBranches(CustomerEntity customer) {
        return orderBundleRepository
                .findByCustomerId(customer.getId())
                .stream()
                .map(entity -> { return entity.getOrder().getId(); })
                .collect(Collectors.toList());
    }

    public void checkOrderRequestQuorum(OrderEntity order, TransportEntity transport)
            throws IllegalArgumentException {

        if (orderBundleRepository.countByOrderId(order.getId()) < transport.getQuorum())
            throw new IllegalArgumentException("Не набрано нужное количество свободных асистентов");
    }
}

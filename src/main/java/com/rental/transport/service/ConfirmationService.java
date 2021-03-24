package com.rental.transport.service;

import com.rental.transport.entity.ConfirmationEntity;
import com.rental.transport.entity.ConfirmationRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmationService {

    @Autowired
    private ConfirmationRepository confirmationRepository;

    public List<OrderEntity> getByCustomer(CustomerEntity customer, Pageable pageable) {

        return confirmationRepository
                .getByCustomerId(customer.getId(), pageable)
                .stream()
                .map(entity -> entity.getOrder())
                .collect(Collectors.toList());
    }

    public List<OrderEntity> getByCustomer(CustomerEntity customer) {

        return confirmationRepository
                .getByCustomerId(customer.getId())
                .stream()
                .map(entity -> entity.getOrder())
                .collect(Collectors.toList());
    }

    public List<OrderEntity> getByOrder(OrderEntity order, Pageable pageable) {

        return confirmationRepository
                .getByOrderId(order.getId(), pageable)
                .stream()
                .map(entity -> entity.getOrder())
                .collect(Collectors.toList());
    }

    public void deleteByOrderId(Long id) {

        confirmationRepository.deleteByOrderId(id);
    }

    public void interaction(CustomerEntity customer, OrderEntity order) {

        confirmationRepository.deleteByCustomerIdAndOrderId(customer.getId(), order.getId());
    }

    @Transactional
    public void putOrder(OrderEntity order) throws IllegalArgumentException {

        order
                .getTransport()
                .getCustomer()
                .stream()
                .forEach(customer -> {
                    ConfirmationEntity entity = new ConfirmationEntity(customer, order);
                    confirmationRepository.save(entity);
                });
    }
}

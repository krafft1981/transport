package com.rental.transport.service;

import com.rental.transport.entity.ConfirmationEntity;
import com.rental.transport.entity.ConfirmationRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmationService {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private ConfirmationRepository confirmationRepository;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    public List<Long> getByCustomer(CustomerEntity customer) {

        return confirmationRepository
                .getByCustomer(customer.getId())
                .stream()
                .map(entity -> entity.getOrder().getId() )
                .collect(Collectors.toList());
    }

    public List<Long> getByOrder(Long id) {

        return confirmationRepository
                .getByOrder(id)
                .stream()
                .map(entity -> entity.getOrder().getId() )
                .collect(Collectors.toList());
    }

    public void deleteByOrderId(Long id) {

        confirmationRepository.deleteByOrderId(id);
    }

    public void interactionCustomerWithOrder(CustomerEntity customer, OrderEntity order) {

        confirmationRepository.deleteByCustomerIdAndOrderId(customer, order);
    }

    @Transactional
    public void putOrder(OrderEntity order) {

        order
                .getDriver()
                .stream()
                .forEach(customer -> {
                    ConfirmationEntity entity = new ConfirmationEntity(customer, order);
                    confirmationRepository.save(entity);
                });
    }

    public ConfirmationEntity get(Long id) throws ObjectNotFoundException {

        ConfirmationEntity entity = confirmationRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Confirmation", id));
        return entity;
    }
}

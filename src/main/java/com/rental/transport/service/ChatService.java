package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.dto.Text;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.MessageEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public Order postOrderMessage(String account, Long orderId, Text body)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        OrderEntity order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Order", orderId));

        if (!order.getCustomer().equals(customer) && !order.getDriver().equals(customer))
            new AccessDeniedException("Message to");

        order.addMessage(new MessageEntity(customer, body.getMessage()));
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

}

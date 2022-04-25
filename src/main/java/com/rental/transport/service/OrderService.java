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
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class OrderService {

    private final CalendarService calendarService;
    private final CustomerService customerService;
    private final OrderRepository orderRepository;
    private final NotifyService notifyService;
    private final OrderMapper orderMapper;

    public List<Order> getOrderByDriver(String account) throws ObjectNotFoundException {

        CustomerEntity driver = customerService.getEntity(account);
        return orderRepository
                .findByDriver(driver.getId())
                .parallelStream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<Order> getOrderByCustomer(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return orderRepository
                .findByCustomer(customer.getId())
                .parallelStream()
                .map(orderMapper::toDto)
                .collect(Collectors.toList());
    }

    public OrderEntity getEntity(Long id) throws ObjectNotFoundException {

        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));
    }

    public Order getOrder(String account, Long id) throws ObjectNotFoundException {

        OrderEntity entity = getEntity(id);
        return orderMapper.toDto(entity);
    }

    @Transactional
    public Order postOrderMessage(String account, Long orderId, Text body)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        OrderEntity order = getEntity(orderId);
        CustomerEntity customer = customerService.getEntity(account);
        MessageEntity message = new MessageEntity(customer, body.getMessage());

        if (order.getDay() < calendarService.getDayIdByTime(new Date().getTime()))
            throw new IllegalArgumentException("Запрос устарел");

        if (!order.getCustomer().equals(customer) && !order.getDriver().equals(customer))
            throw new AccessDeniedException("Message to");

        if (body.getMessage().isEmpty())
            throw new IllegalArgumentException("Введите текст для отправки");

        order.addMessage(message);
        orderRepository.save(order);

        if (order.getCustomer().equals(customer))
            notifyService.messageCreated(order.getDriver());
        else
            notifyService.messageCreated(order.getCustomer());

        return orderMapper.toDto(order);
    }
}

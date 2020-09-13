package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private OrderMapper mapper;

    public Long count() {

        Long count = orderRepository.count();
        return count;
    }

    @Scheduled(fixedDelay = 60000, initialDelay = 0)
    private void scheduleTask() {

        System.out.println("Periodic task run");
    }

    public List<Order> getByPage(@NonNull String account, Pageable pageable) {

        return orderRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public Long create(@NonNull String account, @NonNull Long transportId, Date start, Date stop)
            throws ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerRepository.findByAccount(account);
        if (customer.getFirstName().isEmpty()
                || customer.getLastName().isEmpty()
                || customer.getFamily().isEmpty()
                || customer.getPhone().isEmpty())
            throw new IllegalArgumentException();

        TransportEntity transport = transportRepository
                .findById(transportId)
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", transportId));

        // validate time by orders
        if (orderRepository.countByTransportWhereStartStopBusy(transportId, start, stop) > 0)
            throw new IllegalArgumentException("Временной диапазон занят другим заказом");

        // validate drivers by calendar
        transport
                .getCustomer()
                .stream()
                .forEach(customerEntity -> {
                    calendarService.validateByCustomersEvents(customerEntity, start, stop);
                });

        throw new IllegalArgumentException("Временной диапазон занят в календаре");
/*
        OrderEntity order = new OrderEntity();

        order.setStartAt(start);
        order.setStopAt(stop);

        Long duration = stop.getTime() - start.getTime();
        //validate duration
        if (duration < 1000 * 3600 * 2) {
            throw new IllegalArgumentException("Временной диапазон меньше двух часов");
            // delete from order_request if break
        }

        order.setCost(transport.getCost());

        Long remainder = duration % (1000 * 3600) > 0 ? 1L : 0L;
        order.setPrice(order.getCost() * ((duration / 1000 / 3600) + remainder * 1));

        order.setCustomer(customer);
        order.setCustomerPhone(customer.getPhone());

        StringBuilder builder = new StringBuilder();
        builder.append(customer.getFamily());
        builder.append(" ");
        builder.append(customer.getFirstName());
        builder.append(" ");
        builder.append(customer.getLastName());

        order.setCustomerName(builder.toString());
        order.setTransport(transport.getId());

        return orderRepository.save(order).getId();
*/
    }

    public void confirmOrder(@NonNull String account, Long id)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerRepository.findByAccount(account);

        OrderEntity order = orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Заказ", id));

        TransportEntity transport = transportRepository
                .findById(order.getTransport())
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", order.getTransport()));

        if (transport.getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Подтверждение");

        StringBuilder builder = new StringBuilder();
        builder.append(driver.getFamily());
        builder.append(" ");
        builder.append(driver.getFirstName());
        builder.append(" ");
        builder.append(driver.getLastName());

        order.setDriverName(builder.toString());

        order.setState("Confirmed");
        orderRepository.save(order);
    }

    public List<Order> getByTime(@NonNull String account, Date start, Date stop) {

        return null;
/*
        CustomerEntity customer = customerRepository.findByAccount(account);
        return orderRepository
                .findByCustomerIdAndStartAndStop(customer.getId(), start, stop)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
*/
    }
}

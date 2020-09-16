package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.OrderRequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderRequestRepository orderRequestRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private OrderMapper mapper;

    public List<Order> getByPage(@NonNull String account, Pageable pageable) {

        return orderRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> {
                    return mapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public List<Order> getByTime(Long transportId, Date start, Date stop) {

        return orderRepository
                .findByTransportUseStartAndStop(transportId, start, stop)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public List<Long> getOrderRequestList(String account) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        return orderRequestRepository
                .findByCustomerId(customer.getId())
                .stream()
                .map(entity -> { return entity.getOrder().getId(); })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long create(@NonNull String account, Long transportId, Date start, Date stop)
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
            throw new IllegalArgumentException("Время занято другим заказом");

        // validate duration
        Long duration = stop.getTime() - start.getTime();
        if (duration < 1000 * 3600 * transport.getMinHour())
            throw new IllegalArgumentException("Временной диапазон меньше минимального");

        OrderEntity order = new OrderEntity();

        order.setStartAt(start);
        order.setStopAt(stop);

        order.setCost(transport.getCost());

        // generate price value
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

        order.setLatitude(transport.getLatitude());
        order.setLongitude(transport.getLongitude());

        order.setState("New");

        Long order_id = orderRepository.save(order).getId();

        // validate drivers by calendar
        transport
                .getCustomer()
                .stream()
                .forEach(customerEntity -> { calendarService.сustomerOrderRequest(customerEntity, order, start, stop); });

        if (orderRequestRepository.countByOrderId(order.getId()) < transport.getQuorum())
            throw new IllegalArgumentException("Не набрано нужное количество свободных асистентов");

        return order_id;
    }

    @Transactional
    public void confirmOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerRepository.findByAccount(account);

        OrderEntity order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Заказ", orderId));

        if (order.getState().equals("New") == false)
            throw new IllegalArgumentException("Заказ уже подтверждён");

        TransportEntity transport = transportRepository
                .findById(order.getTransport())
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", order.getTransport()));

        if (transport.getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Подтверждение");

        orderRequestRepository.deleteByOrderIdAndCustomerId(order.getId(), driver.getId());

        Integer confirmed = order.getConfirmed() + 1;
        order.setConfirmed(confirmed);

        order.addDriver(driver);

        if (confirmed >= transport.getQuorum()) {
            orderRequestRepository.deleteByOrderId(order.getId());
            order.setState("Confirmed");
        }
    }

    @Transactional
    public void rejectOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerRepository.findByAccount(account);
        OrderEntity order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Заказ", orderId));

        if (order.getState().equals("New") == false)
            throw new IllegalArgumentException("Заказ уже подтверждён");

        TransportEntity transport = transportRepository
                .findById(order.getTransport())
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", order.getTransport()));

        if (transport.getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Отвержение");

        orderRequestRepository.deleteByOrderId(order.getId());
        orderRepository.deleteById(orderId);
    }
}

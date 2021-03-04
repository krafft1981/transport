package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.MessageEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.OrderStatusEnum;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransportService transportService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private ConfirmationService confirmationService;

    @Autowired
    private OrderMapper orderMapper;

    public List<Order> getByPage(@NonNull String account, Pageable pageable) {

        return orderRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> {
                    return orderMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public List<Order> getByCalendarEvents(Long[] ids)
            throws ObjectNotFoundException {

        List<Order> orders = new ArrayList<>();
        for (Long id : ids) {
            Order order = getByCalendarEvent(id);
            if (Objects.nonNull(order)) {
                orders.add(order);
            }
        }

        return orders;
    }

    public Order getByCalendarEvent(Long id)
            throws ObjectNotFoundException {

        CalendarEntity calendar = calendarService.getEntity(id);
        Long orderId = calendar.getOrder();
        if (Objects.nonNull(orderId)) {
            OrderEntity entity = getEntity(orderId);
            return orderMapper.toDto(entity);
        }

        throw new ObjectNotFoundException("Calendar", id);
    }

    public List<Long> getOrderRequestList(String account) {

        CustomerEntity customer = customerService.getEntity(account);
        return confirmationService.getByCustomer(customer);
    }

    public void sendMessage(String account, Long orderId, String message)
            throws ObjectNotFoundException {

        OrderEntity order = getEntity(orderId);
        CustomerEntity customer = customerService.getEntity(account);
        MessageEntity entity = new MessageEntity(customer, message);
        order.addMessage(entity);
    }

    @Transactional
    public void confirmOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerService.getEntity(account);
        OrderEntity order = getEntity(orderId);

        if (!order.getStatus().equals(OrderStatusEnum.New))
            throw new IllegalArgumentException("Order has status not New");

        if (order.getTransport().getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Confirmation");

        confirmationService.interactionCustomerWithOrder(driver, order);
        Integer confirmed = 0;//er.incConfirmed();
        order.addDriver(driver);

        String quorum = "";//propertyService.getValue(order.getTransport().getProperty(), "quorum");
        if (confirmed >= Long.getLong(quorum)) {
            confirmationService.deleteByOrderId(order.getId());
            order.setStatus(OrderStatusEnum.Confirmed);
        }
    }

    @Transactional
    public void rejectOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerService.getEntity(account);
        OrderEntity order = getEntity(orderId);

        if (!order.getStatus().equals("New"))
            throw new IllegalArgumentException("Order status not New");

        if (!order.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Rejected");

        confirmationService.interactionCustomerWithOrder(driver, order);

        //delete from calendar events by orderId
        confirmationService.deleteByOrderId(order.getId());

        order.setStatus(OrderStatusEnum.Rejected);
    }

    @Transactional
    public Long create(@NonNull String account, Long transportId, Long day, Long start, Long stop)
            throws ObjectNotFoundException, IllegalArgumentException {

        OrderEntity order = new OrderEntity();

        CustomerEntity customer = customerService.getEntity(account);
        order.setCustomer(customer);

        TransportEntity transport = transportService.getEntity(transportId);
        order.setTransport(transport);

        if (transport.getParking().isEmpty())
            throw new IllegalArgumentException("Transport has't parking");

        ParkingEntity parking = transport.getParking().iterator().next();

//        String phone = propertyService.getValue(customer.getProperty(), "phone");
//        String fio = propertyService.getValue(customer.getProperty(), "fio");
//        String minTime = propertyService.getValue(transport.getProperty(), "minTime");
//        String cost = propertyService.getValue(transport.getProperty(), "cost");
//        String latitude = propertyService.getValue(parking.getProperty(), "latitude");
//        String longitude = propertyService.getValue(parking.getProperty(), "longitude");
//
//        propertyService.setValue(order.getProperty(), "fio", fio);
//        propertyService.setValue(order.getProperty(), "phone", phone);
//        propertyService.setValue(order.getProperty(), "latitude", latitude);
//        propertyService.setValue(order.getProperty(), "longitude", longitude);

//        Long total = 0L;
//        for(Long id : eventIds) {
//
//            CalendarEntity event = calendarService.getEntity(id);
//            event.setOrder(order.getId());

        // validate duration
//            Long duration = event.getStopAt().getTime() - event.getStartAt().getTime();
//            if (duration < Integer.parseInt(minTime) * 1000)
//                throw new IllegalArgumentException("Wrong time interval");

//            order.addCalendar(event);
//            total += duration;
//        }

        // generate price value
        // Long remainder = total % 1000 > 0 ? 1L : 0L;
        // order.setPrice(order.getCost() * ((duration / 1000 / 3600) + remainder * 1));

//        propertyService.setValue(order.getProperty(), "duration", total.toString());
//        propertyService.setValue(order.getProperty(), "cost", cost);
//        propertyService.setValue(order.getProperty(), "price", total + " * " + cost);

        Long id = orderRepository.save(order).getId();
        return id;
    }

    public OrderEntity getEntity(Long id) throws ObjectNotFoundException {

        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));
    }

    public Long count() {
        return orderRepository.count();
    }

    public Order getDto(Long id) throws ObjectNotFoundException {

        return orderMapper.toDto(getEntity(id));
    }
}

/*
    // templates
    addProperty(new PropertyEntity("Название", "name", "Название не указано", "String"));
    addProperty(new PropertyEntity("Вместимость", "capacity", "1", "Integer"));
    addProperty(new PropertyEntity("Описание", "description", "Описания нет", "String"));
    addProperty(new PropertyEntity("Цена", "cost", "0", "Double"));
    addProperty(new PropertyEntity("Кворум", "quorum", "1", "Integer"));
    addProperty(new PropertyEntity("Минимальное время аренды", "minTime", "7200", "Integer"));
    addProperty(new PropertyEntity("Нужен водитель", "useDriver", "Yes", "Boolean"));
*/

package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.MessageEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.TransportEntity;
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
    private OrderMapper mapper;

    public List<Order> getByPage(@NonNull String account, Pageable pageable) {

        return orderRepository
                .findAll(pageable)
                .getContent()
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }

    public List<Order> getByCalendarEvent(Long[] ids)
            throws ObjectNotFoundException {

        List<Order> orders = new ArrayList<>();
        for(Long id : ids) {
            Order order = getByCalendarEvent(id);
            if (Objects.nonNull(order)) {
                orders.add(order);
            }
        }

        return orders;
    }

    public Order getByCalendarEvent(Long id)
            throws ObjectNotFoundException {

        CalendarEntity calendar = calendarService.get(id);
        Long orderId = calendar.getOrder();
        if (Objects.nonNull(orderId)) {
            OrderEntity entity = get(orderId);
            Order order = mapper.toDto(entity);
            return order;
        }

        throw new ObjectNotFoundException("Calendar", id);
    }

    public List<Long> getOrderRequestList(String account) {

        CustomerEntity customer = customerService.get(account);
        return confirmationService.getByCustomer(customer);
    }

    @Transactional
    public void sendMessage(String account, Long orderId, String message)
            throws ObjectNotFoundException {

        OrderEntity order = get(orderId);
        CustomerEntity customer = customerService.get(account);
        MessageEntity entity = new MessageEntity(customer, message);
        order.addMessage(entity);

        System.out.println(message + " sended");
    }

    @Transactional
    public void confirmOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerService.get(account);
        OrderEntity order = get(orderId);

        if (!order.getStatus().equals("New"))
            throw new IllegalArgumentException("Order has status not New");

        if (order.getTransport().getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Confirmation");

//        confirmationService. .deleteOrderRequest(order, driver);
//        Integer confirmed = 0;//er.incConfirmed();
        order.addDriver(driver);
//
        String quorum = propertyService.getValue(order.getTransport().getProperty(), "quorum");
//        if (confirmed >= Long.getLong(quorum)) {
//            confirmationService.deleteByOrderId(order.getId());
//            order.setStatus("Confirmed");
//        }
    }

    @Transactional
    public void rejectOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerService.get(account);
        OrderEntity order = get(orderId);

        if (!order.getStatus().equals("New"))
            throw new IllegalArgumentException("Order status not New");

        if (!order.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Rejected");

        //delete from calendar events by orderId
        confirmationService.deleteByOrderId(order.getId());
        orderRepository.deleteById(orderId);
        order.setStatus("Rejected");
    }

    @Transactional
    public Long create(@NonNull String account, Long transportId, Long[] eventIds)
            throws ObjectNotFoundException, IllegalArgumentException {

        OrderEntity order = new OrderEntity();

        CustomerEntity customer = customerService.get(account);
        order.setCustomer(customer);

        TransportEntity transport = transportService.get(transportId);
        order.setTransport(transport);

        if (transport.getParking().isEmpty())
            throw new IllegalArgumentException("Transport has't parking");

        ParkingEntity parking = transport.getParking().iterator().next();

        String phone = propertyService.getValue(customer.getProperty(), "phone");
        String fio = propertyService.getValue(customer.getProperty(), "fio");
        String minTime = propertyService.getValue(transport.getProperty(), "minTime");
        String cost = propertyService.getValue(transport.getProperty(), "cost");
        String latitude = propertyService.getValue(parking.getProperty(), "latitude");
        String longitude = propertyService.getValue(parking.getProperty(), "longitude");

        propertyService.setValue(order.getProperty(), "fio", fio);
        propertyService.setValue(order.getProperty(), "phone", phone);
        propertyService.setValue(order.getProperty(), "latitude", latitude);
        propertyService.setValue(order.getProperty(), "longitude", longitude);

        Long total = 0L;
        for(Long id : eventIds) {

            CalendarEntity event = calendarService.get(id);
            event.setOrder(order.getId());

            // validate duration
            Long duration = event.getStopAt().getTime() - event.getStartAt().getTime();
            if (duration < Integer.parseInt(minTime) * 1000)
                throw new IllegalArgumentException("Wrong time interval");

            order.addCalendar(event);
            total += duration;
        }

        // generate price value
        // Long remainder = total % 1000 > 0 ? 1L : 0L;
        // order.setPrice(order.getCost() * ((duration / 1000 / 3600) + remainder * 1));

        propertyService.setValue(order.getProperty(), "duration", total.toString());
        propertyService.setValue(order.getProperty(), "cost", cost);
        propertyService.setValue(order.getProperty(), "price", total + " * " + cost);

        Long id = orderRepository.save(order).getId();
        return id;
    }

    public OrderEntity get(Long id) throws ObjectNotFoundException {

        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));
    }
}

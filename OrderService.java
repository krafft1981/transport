package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.entity.TransportRepository;
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
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private PropertyService propertyService;

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
            throws ObjectNotFoundException, IllegalArgumentException {

        List<Order> orders = new ArrayList<>();

        for(Long id : ids) {
            CalendarEntity event = calendarService.getEntityById(id);

            if (Objects.isNull(event.getOrder()))
                throw new IllegalArgumentException("Событие без заказа");

            OrderEntity entity = orderRepository
                    .findById(event.getOrder().getId())
                    .orElseThrow(() -> new ObjectNotFoundException("Заказ", event.getOrder().getId()));

            Order order = mapper.toDto(entity);
            orders.add(order);
        }

        return orders;
    }

    public List<Long> getOrderRequestList(String account) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        return orderRequestService.getCustomerRequests(customer);
    }

    @Transactional
    public void rejectOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerRepository.findByAccount(account);
        OrderEntity order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Заказ", orderId));

        if (!order.getStatus().equals("New"))
            throw new IllegalArgumentException("Заказ не новый");

        if (!order.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Rejected");

        //delete from calendar events by orderId
        orderRequestService.deleteOrderRequest(order);
        orderRepository.deleteById(orderId);
    }

    @Transactional
    public void confirmOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerRepository.findByAccount(account);

        OrderEntity order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Заказ", orderId));

        if (!order.getStatus().equals("New"))
            throw new IllegalArgumentException("Заказ не новый");

        if (order.getTransport().getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Подтверждение");

        orderRequestService.deleteOrderRequest(order, driver);

        Integer confirmed = order.incConfirmed();

        order.addDriver(driver);

        String quorum = propertyService.getValue(order.getTransport().getProperty(), "quorum");
        if (confirmed >= Long.getLong(quorum)) {
            orderRequestService.deleteOrderRequest(order);
            order.setStatus("Confirmed");
        }
    }

    @Transactional
    public Long create(@NonNull String account, Long transportId, Long[] eventIds)
            throws ObjectNotFoundException, IllegalArgumentException {

        OrderEntity order = new OrderEntity();

        CustomerEntity customer = customerRepository.findByAccount(account);
        order.setCustomer(customer);

        String phone = propertyService.getValue(customer.getProperty(), "phone");
        String fio = propertyService.getValue(customer.getProperty(), "fio");

        TransportEntity transport = transportRepository
                .findById(transportId)
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", transportId));

        order.setTransport(transport);

        if (transport.getParking().isEmpty())
            throw new IllegalArgumentException("Транспорт не имеет стоянки");

        ParkingEntity parking = transport.getParking().iterator().next();

        String price = propertyService.getValue(transport.getProperty(), "price");
        String latitude = propertyService.getValue(parking.getProperty(), "latitude");
        String longitude = propertyService.getValue(parking.getProperty(), "longitude");
        for(Long id : eventIds) {


//        CalendarEntity event = calendarService.getEntityById(eventId);

        // validate duration
//        Long duration = event.getStopAt().getTime() - event.getStartAt().getTime();

//        if (duration < 1000 * 3600 * transport.getMinHour())
//            throw new IllegalArgumentException("Временной диапазон меньше минимального");

//        order.addCalendar(event);

//        order.setCost(transport.getCost());

        // generate price value
//        Long remainder = duration % (1000 * 3600) > 0 ? 1L : 0L;
//        order.setPrice(order.getCost() * ((duration / 1000 / 3600) + remainder * 1));

        }
        Long order_id = orderRepository.save(order).getId();
        return order_id;
    }
}

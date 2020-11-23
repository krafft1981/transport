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
import java.util.stream.Collectors;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService extends PropertyService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderBundleService orderBundleService;

    @Autowired
    private TransportRepository transportRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private OrderMapper mapper;

    public OrderService() {
        setProp("state", "New");
    }

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

    public List<Order> getByCalendarEvent(Long[] ids)
            throws ObjectNotFoundException, IllegalArgumentException {

        List<Order> orders = new ArrayList<>();

        for(Long id : ids) {
            CalendarEntity event = calendarService.getEntityById(id);
/*
            if (Objects.isNull(event.getOrderId()))
                throw new IllegalArgumentException("Событие без заказа");

            OrderEntity order = orderRepository
                    .findById(event.getOrderId())
                    .orElseThrow(() -> new ObjectNotFoundException("Заказ", event.getOrderId()));

            orders.add(mapper.toDto(order));
*/
        }

        return orders;
    }

    public List<Long> getOrderRequestList(String account) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        return orderBundleService.getCustomerBranches(customer);
    }

    @Transactional
    public Long create(@NonNull String account, Long transportId, Long eventId)
            throws ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerRepository.findByAccount(account);
/*
        if (customer.getFirstName().isEmpty()
                || customer.getLastName().isEmpty()
                || customer.getFamily().isEmpty()
                || customer.getPhone().isEmpty())
            throw new IllegalArgumentException();
*/
        TransportEntity transport = transportRepository
                .findById(transportId)
                .orElseThrow(() -> new ObjectNotFoundException("Транспорт", transportId));

        CalendarEntity event = calendarService.getEntityById(eventId);

        // validate duration
        Long duration = event.getStopAt().getTime() - event.getStartAt().getTime();
//        if (duration < 1000 * 3600 * transport.getMinHour())
//            throw new IllegalArgumentException("Временной диапазон меньше минимального");

        OrderEntity order = new OrderEntity();

//        order.addCalendar(event);

//        order.setCost(transport.getCost());

        // generate price value
        Long remainder = duration % (1000 * 3600) > 0 ? 1L : 0L;
//        order.setPrice(order.getCost() * ((duration / 1000 / 3600) + remainder * 1));

        order.setCustomer(customer);
//        order.setCustomerPhone(customer.getPhone());

        StringBuilder builder = new StringBuilder();
/*
        builder.append(customer.getFamily());
        builder.append(" ");
        builder.append(customer.getFirstName());
        builder.append(" ");
        builder.append(customer.getLastName());

        order.setCustomerName(builder.toString());
 */
        order.setTransport(transport);

        if (transport.getParking().isEmpty())
            throw new IllegalArgumentException("Транспорт не имеет стоянки");

        ParkingEntity parking = transport.getParking().iterator().next();

//        order.setLatitude(parking.getLatitude());
//        order.setLongitude(parking.getLongitude());

        Long order_id = orderRepository.save(order).getId();

        orderBundleService.checkOrderRequestQuorum(order, transport);
        return order_id;
    }

    @Transactional
    public void confirmOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerRepository.findByAccount(account);

        OrderEntity order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Заказ", orderId));

//        if (order.getState().equals("New") == false)
//            throw new IllegalArgumentException("Заказ не новый");

        if (order.getTransport().getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Подтверждение");

        orderBundleService.deleteOrderBranch(order, driver);

//        Integer confirmed = order.getConfirmed() + 1;
//        order.setConfirmed(confirmed);

//        order.addDriver(driver);

//        if (confirmed >= order.getTransport().getQuorum()) {
//            orderBundleService.deleteOrderBundle(order);
//            order.setState("Confirmed");
//        }
    }

    @Transactional
    public void rejectOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerRepository.findByAccount(account);
        OrderEntity order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Заказ", orderId));

//        if (order.getState().equals("New") == false)
//            throw new IllegalArgumentException("Заказ не новый");

        if (order.getTransport().getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Rejected");

        //delete from calendar events by orderId
        orderBundleService.deleteOrderBundle(order);
        orderRepository.deleteById(orderId);
    }
}

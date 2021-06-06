package com.rental.transport.service;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Order;
import com.rental.transport.dto.Request;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.RequestEntity;
import com.rental.transport.entity.RequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.enums.RequestStatusEnum;
import com.rental.transport.mapper.CalendarMapper;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.mapper.RequestMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private TransportService transportService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private RequestRepository requestRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private CalendarMapper calendarMapper;

    @Scheduled(cron = "0 0 * * * ?")
    public void setRequestExpired() {

        requestRepository.setExpired();
    }

    private void setInteracted(RequestEntity request, CustomerEntity driver, Long orderId) {

        requestRepository
                .findNewByCustomerAndTransportAndDay(
                        request.getCustomer().getId(),
                        request.getTransport().getId(),
                        request.getDay()
                )
                .stream()
                .forEach(entity -> {
                    if (Objects.nonNull(driver) && entity.getDriver().equals(driver)) {
                        entity.setOrder(orderId);
                        entity.setInteract(RequestStatusEnum.ACCEPTED);
                    }
                    else
                        entity.setInteract(RequestStatusEnum.REJECTED);
                });
    }

    public Map<Integer, Event> getOrderByCustomer(String account, Pageable pageable)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        Map<Integer, Event> result = new HashMap();
        orderRepository
                .findByCustomerOrderByIdDesc(customer, pageable)
                .stream()
                .forEach(entity -> {
                    for (Integer hour : entity.getHours())
                        result.put(hour, new Event(orderMapper.toDto(entity)));
                });

        return result;
    }

    public Map<Integer, Event> getOrderByMyTransport(String account, Pageable pageable)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        Map<Integer, Event> result = new HashMap();
        for (TransportEntity transport : customer.getTransport()) {
            orderRepository
                    .findByTransportOrderByIdDesc(transport, pageable)
                    .stream()
                    .forEach(entity -> {
                        for (Integer hour : entity.getHours())
                            result.put(hour, new Event(orderMapper.toDto(entity)));
                    });
        }

        return result;
    }

    public List<Request> getRequestAsCustomer(String account) {

        CustomerEntity customer = customerService.getEntity(account);
        return requestRepository
                .findRequestByCustomer(customer.getId())
                .stream()
                .map(entity -> requestMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Request> getRequestAsDriver(String account) {

        CustomerEntity driver = customerService.getEntity(account);
        return requestRepository
                .findRequestByDriver(driver.getId())
                .stream()
                .map(entity -> requestMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public OrderEntity getEntity(Long id) throws ObjectNotFoundException {

        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));
    }

    @Transactional
    public Event postAbsentCustomerEntry(String account, Long day, Integer[] hours)
            throws IllegalArgumentException, ObjectNotFoundException {

        Event event = new Event(EventTypeEnum.UNAVAILABLE, day, hours);
        return event;

//        CustomerEntity customer = customerService.getEntity(account);
//        calendarService.checkCustomerBusy(customer, day, start, stop);
//        CalendarEntity calendar = calendarService.getEntity(day, start, stop, true);
//        customer.addCalendar(calendar);

//        // Отменяем все запросы на это время если все остальные водители заняты
//        customer
//                .getTransport()
//                .stream()
//                .forEach(transport -> {
//                    Boolean delete = true;
//                    for (CustomerEntity driver : transport.getCustomer()) {
//                        if (driver.equals(customer))
//                            continue;

//                        try {
//                            calendarService.checkCustomerBusy(
//                                    driver,
//                                    calendar.getDayNum(),
//                                    calendar.getStartAt().getTime(),
//                                    calendar.getStopAt().getTime()
//                            );

//                            delete = false;
//                        }
//                        catch (IllegalArgumentException e) {
//                            System.out.println(e);
//                        }
//                    }

//                    if (delete) {
//                        //удаляем все запросы с пересекающимся временем
//                    }
//                });

//        return calendar.getId();
    }

    @Transactional
    public Long putAbsentCustomerEntry(String name, Long id, String message) {

        return 0L;
    }

    @Transactional
    public void deleteAbsentCustomerEntry(String account, Long[] ids)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        for (Long id : ids) {
            CalendarEntity calendar = calendarService.getEntity(id);
            customer.deleteCalendarEntity(calendar);
        }
    }

    public List<Order> getOrder(String account) {

        CustomerEntity customer = customerService.getEntity(account);
//        orderRepository.findByCustomerOrderByIdDesc(customer);
        return new ArrayList();
    }

    @Transactional
    public Map<Integer, Event> createRequest(String account, Long transportId, Long day, Integer[] hours)
            throws ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);

        Date now = new Date();
        day = calendarService.getDayId(day);

        if (day < calendarService.getDayId(now.getTime()))
            throw new IllegalArgumentException("Нельзя редактировать прошлое");

        requestRepository.deleteByCustomerAndTransportByDay(customer.getId(), transport.getId(), day);

        if (transport.getParking().isEmpty())
            throw new IllegalArgumentException("Данный транспорт не имеет стоянки");

        if (Objects.nonNull(hours)) {
            Integer minTime = Integer.parseInt(propertyService.getValue(transport.getProperty(), "transport_min_rent_time"));
            if (hours.length < minTime)
                throw new IllegalArgumentException("Заказ должен состоять из не менее чем " + minTime + " часов последовательно");

            Arrays.sort(hours);
            Integer current = null;
            for (Integer hour : hours) {
                if (current == null) {
                    current = hour;
                    continue;
                }

                if ((current + 1) != hour)
                    throw new IllegalArgumentException("Выберите часы последовательно");

                current = hour;
            }

            for (CustomerEntity driver : transport.getCustomer()) {
                RequestEntity request = new RequestEntity(customer, driver, transport, day, hours);
                calendarService.checkCustomerBusy(customer, day, hours);
                calendarService.checkTransportBusy(transport, day, hours);
                requestRepository.save(request);
            }
        }

        return getTransportCalendar(account, day, transportId);
    }

    public Map<Integer, Event> getTransportCalendar(String account, Long day, Long transportId) {

        TransportEntity transport = transportService.getEntity(transportId);
        CustomerEntity customer = customerService.getEntity(account);
        return calendarService.getTransportCalendar(customer, transport, day);
    }

    public Map<Integer, Event> getCustomerCalendarWithOrders(String account, Long day) {

        CustomerEntity customer = customerService.getEntity(account);
        return calendarService.getCustomerCalendarWithOrders(day, customer);
    }

    @Transactional
    public void confirmRequest(String account, Long requestId)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        CustomerEntity driver = customerService.getEntity(account);
        RequestEntity request = requestRepository
                .findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Event", requestId));

        if (!request.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Confirmation");

        if (request.getStatus() != RequestStatusEnum.NEW)
            throw new IllegalArgumentException("Try update non new request");

        CustomerEntity customer = request.getCustomer();
        TransportEntity transport = request.getTransport();
        ParkingEntity parking = transport.getParking().iterator().next();

        // validate future
        Date now = new Date();

        if ((request.getDay() < now.getTime()))
            throw new IllegalArgumentException("Allow orders only in the future");

        OrderEntity order = new OrderEntity(customer, transport, driver, request.getDay(), request.getHours());

        calendarService.checkCustomerBusy(
                driver,
                request.getDay(),
                request.getHours()
        );

        calendarService.checkCustomerBusy(
                customer,
                request.getDay(),
                request.getHours()
        );

        calendarService.checkTransportBusy(
                transport,
                request.getDay(),
                request.getHours()
        );

        String price = propertyService.getValue(transport.getProperty(), "transport_price");

        // calculate cost
        Integer min = Collections.min(Arrays.asList(request.getHours()));
        Integer max = Collections.max(Arrays.asList(request.getHours()));
        Integer duration = max - min;
        double cost = (duration + 1) * Double.parseDouble(price);

        order.addProperty(
                propertyService.copy("order_parking_name", parking.getProperty(), "parking_name"),
                propertyService.copy("order_parking_latitude", parking.getProperty(), "parking_latitude"),
                propertyService.copy("order_parking_longitude", parking.getProperty(), "parking_longitude"),
                propertyService.copy("order_parking_address", parking.getProperty(), "parking_address"),
                propertyService.copy("order_parking_locality", parking.getProperty(), "parking_locality"),
                propertyService.copy("order_parking_region", parking.getProperty(), "parking_region"),

                propertyService.copy("order_transport_name", transport.getProperty(), "transport_name"),
                propertyService.copy("order_transport_capacity", transport.getProperty(), "transport_capacity"),
                propertyService.copy("order_transport_price", transport.getProperty(), "transport_price"),
                propertyService.create("order_transport_cost", String.format("%.2f", cost)),

                propertyService.copy("order_customer_fio", customer.getProperty(), "customer_fio"),
                propertyService.copy("order_customer_phone", customer.getProperty(), "customer_phone")
        );

        CalendarEntity calendar = calendarService.getEntity(request.getDay(), request.getHours());

        customer.addCalendar(calendar);
        transport.addCalendar(calendar);

        order.setCustomer(customer);
        order.setTransport(transport);
        order.setDay(request.getDay());
        order.setHours(request.getHours());
        order.setDriver(driver);

        driver.addCalendar(calendar);

        orderRepository.save(order);
        request.setOrder(order.getId());

        // Водитель нашёлся
        setInteracted(request, driver, order.getId());

        // Отменяем все запросы по данному транспорту в это время
        requestRepository
                .findNewRequestByTransportAndDay(transport.getId(), request.getDay())
                .stream()
                .forEach(entity -> {
                    if (entity.getDay().equals(request.getDay())) {
                        try {
//                            calendarService.checkTimeDiapazon(entity.getCalendar(), calendar);
                        }
                        catch (IllegalArgumentException e) {
                            entity.setInteract(RequestStatusEnum.REJECTED);
                        }
                    }
                });
    }

    @Transactional
    public void rejectRequest(String account, Long requestId)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        RequestEntity request = requestRepository
                .findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Event", requestId));

        CustomerEntity customer = customerService.getEntity(account);

        if (!request.getTransport().getCustomer().contains(customer) && !request.getCustomer().equals(customer))
            throw new AccessDeniedException("Confirmation");

        if (request.getStatus() != RequestStatusEnum.NEW)
            throw new IllegalArgumentException("Try update non new request");

        setInteracted(request, null, null);
    }

    @PostConstruct
    public void postConstruct() {

        propertyService.createType("order_parking_name", "Название стоянки", PropertyTypeEnum.String);
        propertyService.createType("order_parking_latitude", "Широта", PropertyTypeEnum.Double);
        propertyService.createType("order_parking_longitude", "Долгота", PropertyTypeEnum.Double);
        propertyService.createType("order_parking_address", "Адрес стоянки", PropertyTypeEnum.String);
        propertyService.createType("order_parking_locality", "Местонахождение", PropertyTypeEnum.String);
        propertyService.createType("order_parking_region", "Район", PropertyTypeEnum.String);

        propertyService.createType("order_transport_name", "Название транспорта", PropertyTypeEnum.String);
        propertyService.createType("order_transport_capacity", "Количество гостей", PropertyTypeEnum.Integer);

        propertyService.createType("order_transport_cost", "Стоимость заказа", PropertyTypeEnum.Double);
        propertyService.createType("order_transport_price", "Стоимость за час", PropertyTypeEnum.Double);

        propertyService.createType("order_customer_fio", "Имя заказчика", PropertyTypeEnum.String);
        propertyService.createType("order_customer_phone", "Сотовый заказчика", PropertyTypeEnum.Phone);

        propertyService.createType("order_driver_fio", "Имя капитана", PropertyTypeEnum.String);
        propertyService.createType("order_driver_phone", "Сотовый капитана", PropertyTypeEnum.Phone);
    }
}
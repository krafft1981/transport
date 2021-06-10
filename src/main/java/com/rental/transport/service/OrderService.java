package com.rental.transport.service;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Order;
import com.rental.transport.dto.Request;
import com.rental.transport.dto.Text;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.MessageEntity;
import com.rental.transport.entity.MessageRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
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
    private MessageRepository messageRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private CalendarMapper calendarMapper;

    @Scheduled(cron = "0 0 * * * *")
    public void setRequestExpired() {

        requestRepository.setExpired();
    }

    private CustomerEntity getCustomerByAccount(String account) throws ObjectNotFoundException {

        return customerService.getEntity(account);
    }

    private void setInteracted(RequestEntity request, CustomerEntity driver, Long orderId) {

        requestRepository
                .updateNewByDay(
                        request.getCustomer().getId(),
                        request.getTransport().getId(),
                        request.getDay()
                )
                .stream()
                .forEach(entity -> {
                    if (Objects.nonNull(driver) && entity.getDriver().equals(driver)) {
                        entity.setOrder(orderId);
                        entity.setInteract(RequestStatusEnum.ACCEPTED);
                    } else
                        entity.setInteract(RequestStatusEnum.REJECTED);
                });
    }

    public List<Order> getOrderByDriver(String account) throws ObjectNotFoundException {

        CustomerEntity driver = getCustomerByAccount(account);
        return orderRepository
                .findByDriver(driver.getId())
                .stream()
                .map(entity -> orderMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Order> getOrderByCustomer(String account) throws ObjectNotFoundException {

        CustomerEntity customer = getCustomerByAccount(account);
        return orderRepository
                .findByCustomer(customer.getId())
                .stream()
                .map(entity -> orderMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Order> getOrderByMyTransport(String account) throws ObjectNotFoundException {

        CustomerEntity customer = getCustomerByAccount(account);
        List<Order> result = new ArrayList();
        for (TransportEntity transport : customer.getTransport()) {
            orderRepository
                    .findByTransport(transport.getId())
                    .stream()
                    .forEach(entity -> result.add(orderMapper.toDto(entity)));
        }

        return result;
    }

    public List<Request> getRequestAsCustomer(String account) throws ObjectNotFoundException {

        CustomerEntity customer = getCustomerByAccount(account);
        return requestRepository
                .findRequestByCustomer(customer.getId())
                .stream()
                .map(entity -> requestMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Request> getRequestAsDriver(CustomerEntity driver) {

        return requestRepository
                .findRequestByDriver(driver.getId())
                .stream()
                .map(entity -> requestMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Request> getRequestAsDriver(String account) throws ObjectNotFoundException {

        CustomerEntity driver = getCustomerByAccount(account);
        return getRequestAsDriver(driver);
    }

    public OrderEntity getEntity(Long id) throws ObjectNotFoundException {

        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));
    }

    @Transactional
    public Event postAbsentCustomerEntry(String account, Long day, Integer[] hours)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        calendarService.checkCustomerBusy(customer, day, hours);
        CalendarEntity calendar = calendarService.getEntity(day, hours);
        customer.addCalendar(calendar);
        return new Event(EventTypeEnum.NOTEBOOK, day, hours);
    }

    @Transactional
    public Long putAbsentCustomerEntry(String account, Long id, Text body) {

        CustomerEntity customer = customerService.getEntity(account);

        return 0L;
    }

    @Transactional
    public void deleteAbsentCustomerEntry(String account, Long[] ids)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = getCustomerByAccount(account);
        for (Long id : ids) {
            CalendarEntity calendar = calendarService.getEntity(id);
            customer.deleteCalendarEntity(calendar);
        }
    }

    public Map<Integer, Event> getTransportCalendar(String account, Long day, Long transportId) throws ObjectNotFoundException {

        TransportEntity transport = transportService.getEntity(transportId);
        CustomerEntity customer = getCustomerByAccount(account);
        return calendarService.getTransportCalendar(customer, transport, day);
    }

    public Map<Integer, Event> getCustomerCalendar(String account, Long day) throws ObjectNotFoundException {

        CustomerEntity customer = getCustomerByAccount(account);
        return calendarService.getDriverCalendarWithOrders(day, customer);
    }

    @Transactional
    public Map<Integer, Event> createRequest(String account, Long transportId, Long day, Integer[] hours)
            throws ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = getCustomerByAccount(account);
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
                throw new IllegalArgumentException("Выберите не менее чем " + minTime + " часов последовательно");

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

            calendarService.checkCustomerBusy(customer, day, hours);

            for (CustomerEntity driver : transport.getCustomer()) {
                calendarService.checkDriverBusy(transport, day, hours);
                calendarService.checkTransportBusy(transport, day, hours);
                requestRepository.save(new RequestEntity(customer, driver, transport, day, hours));
            }
        }

        return getTransportCalendar(account, day, transportId);
    }

    @Transactional
    public List<Request> confirmRequest(String account, Long requestId)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        CustomerEntity driver = getCustomerByAccount(account);
        RequestEntity request = requestRepository
                .findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Event", requestId));

        if (!request.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Confirmation");

        if (request.getStatus() != RequestStatusEnum.NEW)
            throw new IllegalArgumentException("Вы пытаетесь ответить на не новый запрос");

        CustomerEntity customer = request.getCustomer();
        TransportEntity transport = request.getTransport();
        ParkingEntity parking = transport.getParking().iterator().next();

        // validate future
        Date now = new Date();

        if ((request.getDay() < now.getTime()))
            throw new IllegalArgumentException("Нельзя создавать заказы в прошлое");

        OrderEntity order = new OrderEntity(customer, transport, driver, request.getDay(), request.getHours());

        calendarService.checkCustomerBusy(driver, request.getDay(), request.getHours());
        calendarService.checkCustomerBusy(customer, request.getDay(), request.getHours());
        calendarService.checkTransportBusy(transport, request.getDay(), request.getHours());

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
                propertyService.create("order_transport_type", transport.getType().getName()),
                propertyService.copy("order_transport_name", transport.getProperty(), "transport_name"),
                propertyService.copy("order_transport_capacity", transport.getProperty(), "transport_capacity"),
                propertyService.copy("order_transport_price", transport.getProperty(), "transport_price"),
                propertyService.create("order_transport_cost", String.format("%.2f", cost)),
                propertyService.copy("order_driver_fio", driver.getProperty(), "customer_fio"),
                propertyService.copy("order_driver_phone", driver.getProperty(), "customer_phone"),
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

        // Отменяем все запросы по данному транспорту в это время (Не готово)
        requestRepository
                .findNewRequestByTransportAndDay(transport.getId(), request.getDay())
                .stream()
                .forEach(entity -> {
                });

        requestRepository
                .findNewRequestByCustomerAndDay(transport.getId(), request.getDay())
                .stream()
                .forEach(entity -> {
                });

        requestRepository
                .findNewRequestByDriverAndDay(transport.getId(), request.getDay())
                .stream()
                .forEach(entity -> {
                });

        return getRequestAsDriver(driver);
    }

    @Transactional
    public List<Request> rejectRequest(String account, Long requestId)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        RequestEntity request = requestRepository
                .findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Event", requestId));

        CustomerEntity driver = getCustomerByAccount(account);

        if (!request.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Confirmation");

        if (request.getStatus() != RequestStatusEnum.NEW)
            throw new IllegalArgumentException("Вы пытаетесь ответить на не новый запрос");

        setInteracted(request, null, null);
        return getRequestAsDriver(driver);
    }

    @Transactional
    public Order postOrderMessage(String account, Long orderId, Text body)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity customer = getCustomerByAccount(account);
        OrderEntity order = orderRepository
                .findById(orderId)
                .orElseThrow(() -> new ObjectNotFoundException("Order", orderId));

        if (!order.getCustomer().equals(customer) && !order.getDriver().equals(customer))
            new AccessDeniedException("Message to");

        MessageEntity message = new MessageEntity(customer, body.getMessage());
        order.addMessage(message);
        orderRepository.save(order);
        return orderMapper.toDto(order);
    }

    public Order getOrder(String account, Long id) throws ObjectNotFoundException {

        CustomerEntity customer = getCustomerByAccount(account);
        OrderEntity entity = orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));

        return orderMapper.toDto(entity);
    }

    @PostConstruct
    public void postConstruct() {

        propertyService.createType("order_parking_name", "Название стоянки", PropertyTypeEnum.String);
        propertyService.createType("order_parking_latitude", "Широта", PropertyTypeEnum.Double);
        propertyService.createType("order_parking_longitude", "Долгота", PropertyTypeEnum.Double);
        propertyService.createType("order_parking_address", "Адрес стоянки", PropertyTypeEnum.String);
        propertyService.createType("order_parking_locality", "Местонахождение", PropertyTypeEnum.String);
        propertyService.createType("order_parking_region", "Район", PropertyTypeEnum.String);

        propertyService.createType("order_transport_type", "Тип транспорта", PropertyTypeEnum.String);
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
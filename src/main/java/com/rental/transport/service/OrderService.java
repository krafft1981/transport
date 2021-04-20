package com.rental.transport.service;

import com.rental.transport.dto.Event;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.RequestEntity;
import com.rental.transport.entity.RequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.enums.RequestStatusEnum;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.mapper.RequestMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
    private OrderMapper orderMapper;

    @Autowired
    private RequestMapper requestMapper;

    private void setInteracted(RequestEntity request, CustomerEntity driver, Long orderId) {

        requestRepository
                .findByCustomerAndTransportAndCalendarAndStatus(
                        request.getCustomer(),
                        request.getTransport(),
                        request.getCalendar(),
                        RequestStatusEnum.NEW
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
//        return orderRepository
//                .findByCustomerOrderByIdDesc(customer, pageable)
//                .stream()
//                .map(entity ->
//                        new Event(
//                                orderMapper.toDto(entity),
//                                calendarMapper.toDto(entity.getCalendar().iterator().next())
//                        )
//                )
//                .collect(Collectors.toMap());
        return null;
    }

    public Map<Integer, Event> getOrderByMyTransport(String account, Pageable pageable) {

        return null;
//        return customerService
//                .getEntity(account)
//                .getTransport()
//                .stream()
//                .map(transport -> orderRepository
//                        .findByTransportOrderByIdDesc(transport, pageable)
//                        .stream()
//                        .map(entity ->
//                                new Event(
//                                        orderMapper.toDto(entity),
//                                        calendarMapper.toDto(entity.getCalendar().iterator().next())
//                                )
//                        )
//                        .collect(Collectors.toList())
//                )
//                .flatMap(x -> x.stream())
//                .collect(Collectors.toList());
    }

    public Map<Integer, Event> getRequestAsCustomer(String account, Pageable pageable) {

        CustomerEntity customer = customerService.getEntity(account);
        return null;
//        return requestRepository
//                .findByCustomer(customer, pageable)
//                .stream()
//                .map(entity -> new Event(
//                        requestMapper.toDto(entity),
//                        calendarMapper.toDto(entity.getCalendar()))
//                )
//                .collect(Collectors.toList());
    }

    public Map<Integer, Event> getRequestAsDriver(String account, Pageable pageable) {

        CustomerEntity driver = customerService.getEntity(account);
        return null;
//        return requestRepository
//                .findByDriver(driver, pageable)
//                .stream()
//                .map(entity -> new Event(
//                        requestMapper.toDto(entity),
//                        calendarMapper.toDto(entity.getCalendar()))
//                )
//                .collect(Collectors.toList());
    }

    public OrderEntity getEntity(Long id) throws ObjectNotFoundException {

        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));
    }

    @Transactional
    public Long putAbsentCustomerEntry(String account, Long day, Integer[] hours)
            throws IllegalArgumentException, ObjectNotFoundException {

        return null;

//        CustomerEntity customer = customerService.getEntity(account);
//        calendarService.checkCustomerBusy(customer, day, start, stop);
//        CalendarEntity calendar = calendarService.getEntity(day, start, stop, true);
//        customer.addCalendar(calendar);
//
//        // Отменяем все запросы на это время если все остальные водители заняты
//        customer
//                .getTransport()
//                .stream()
//                .forEach(transport -> {
//                    Boolean delete = true;
//                    for (CustomerEntity driver : transport.getCustomer()) {
//                        if (driver.equals(customer))
//                            continue;
//
//                        try {
//                            calendarService.checkCustomerBusy(
//                                    driver,
//                                    calendar.getDayNum(),
//                                    calendar.getStartAt().getTime(),
//                                    calendar.getStopAt().getTime()
//                            );
//
//                            delete = false;
//                        }
//                        catch (IllegalArgumentException e) {
//                            System.out.println(e);
//                        }
//                    }
//
//                    if (delete) {
//                        //удаляем все запросы с пересекающимся временем
//                    }
//                });
//
//        return calendar.getId();
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

    @Transactional
    public void createRequest(String account, Long transportId, Long day, Integer[] hours)
            throws ObjectNotFoundException, IllegalArgumentException {

        TransportEntity transport = transportService.getEntity(transportId);
        CustomerEntity customer = customerService.getEntity(account);

        requestRepository.deleteByCustomerAndTransportByDay(customer.getId(), transport.getId(), day);

        if (transport.getParking().isEmpty())
            throw new IllegalArgumentException("Transport has't parking");

        Integer minTime = Integer.parseInt(propertyService.getValue(transport.getProperty(), "transport_min_rent_time"));
        if (hours.length < minTime)
            throw new IllegalArgumentException("Wrong transport rent time");

        Integer followCnt = hours.length;
        Arrays.sort(hours);
        Integer current = null;
        for (Integer hour : hours) {
            if (current == null) {
                current = hour;
                continue;
            }

            if ((current + 1) != hour)
                followCnt--;

            current = hour;
        }

        if (followCnt < minTime)
            throw new IllegalArgumentException("Wrong time sequence");

        for (Integer hour : hours) {
            CalendarEntity calendar = calendarService.getEntity(day, hour, true);

            // TODO проверки на занятость
//        calendarService.checkCustomerBusy(customer, day, start, stop);
//        calendarService.checkTransportBusy(transport, day, start, stop);

            for (CustomerEntity driver : transport.getCustomer()) {
                RequestEntity entity = new RequestEntity(customer, driver, transport, calendar);
                requestRepository.save(entity);
            }
        }
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
        CalendarEntity calendar = request.getCalendar();
        ParkingEntity parking = transport.getParking().iterator().next();

//        // validate future
//        Date now = new Date();
//        if (now.after(calendar.getStartAt())) {
//            setInteracted(request, null, null);
//            throw new IllegalArgumentException("Allow orders only in the future");
//        }
//
//        calendarService.checkCustomerBusy(
//                driver,
//                calendar.getDayNum(),
//                calendar.getStartAt().getTime(),
//                calendar.getStopAt().getTime()
//        );
//
//        calendarService.checkCustomerBusy(
//                customer,
//                calendar.getDayNum(),
//                calendar.getStartAt().getTime(),
//                calendar.getStopAt().getTime()
//        );
//
//        calendarService.checkTransportBusy(
//                transport,
//                calendar.getDayNum(),
//                calendar.getStartAt().getTime(),
//                calendar.getStopAt().getTime()
//        );
//
//        OrderEntity order = new OrderEntity();
//
//        String price = propertyService.getValue(transport.getProperty(), "transport_price");
//
//        // calculate cost
//        Long interval = (calendar.getStopAt().getTime() - calendar.getStartAt().getTime()) / 1000;
//        double cost = Math.ceil(interval / 3600) * Double.parseDouble(price);
//
//        order.addProperty(
//                propertyService.copy("order_parking_name", parking.getProperty(), "parking_name"),
//                propertyService.copy("order_parking_latitude", parking.getProperty(), "parking_latitude"),
//                propertyService.copy("order_parking_longitude", parking.getProperty(), "parking_longitude"),
//                propertyService.copy("order_parking_address", parking.getProperty(), "parking_address"),
//                propertyService.copy("order_parking_locality", parking.getProperty(), "parking_locality"),
//                propertyService.copy("order_parking_region", parking.getProperty(), "parking_region"),
//
//                propertyService.copy("order_transport_name", transport.getProperty(), "transport_name"),
//                propertyService.copy("order_transport_capacity", transport.getProperty(), "transport_capacity"),
//                propertyService.copy("order_transport_price", transport.getProperty(), "transport_price"),
//                propertyService.create("order_transport_cost", String.format("%.2f", cost)),
//
//                propertyService.copy("order_customer_fio", customer.getProperty(), "customer_fio"),
//                propertyService.copy("order_customer_phone", customer.getProperty(), "customer_phone")
//        );
//
//        customer.addCalendar(calendar);
//        transport.addCalendar(calendar);
//
//        order.setCustomer(customer);
//        order.setTransport(transport);
//        order.addCalendar(calendar);
//        order.addDriver(driver);
//
//        driver.addCalendar(calendar);
//
//        orderRepository.save(order);
//
//        // Водитель нашёлся
//        setInteracted(request, driver, order.getId());
//
//        // Отменяем все запросы по данному транспорту в это время
//        requestRepository
//                .findByTransportAndStatus(transport, RequestStatusEnum.NEW)
//                .stream()
//                .forEach(entity -> {
//                    if (entity.getCalendar().getDayNum().equals(calendar.getDayNum())) {
//                        try {
//                            calendarService.checkTimeDiapazon(entity.getCalendar(), calendar);
//                        }
//                        catch (IllegalArgumentException e) {
//                            entity.setInteract(RequestStatusEnum.REJECTED);
//                        }
//                    }
//                });
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

        propertyService.createType("order_parking_name", "Название", PropertyTypeEnum.String);
        propertyService.createType("order_parking_latitude", "Широта", PropertyTypeEnum.Double);
        propertyService.createType("order_parking_longitude", "Долгота", PropertyTypeEnum.Double);
        propertyService.createType("order_parking_address", "Адрес", PropertyTypeEnum.String);
        propertyService.createType("order_parking_locality", "Местонахождение", PropertyTypeEnum.String);
        propertyService.createType("order_parking_region", "Район", PropertyTypeEnum.String);

        propertyService.createType("order_transport_name", "Название транспорта", PropertyTypeEnum.String);
        propertyService.createType("order_transport_capacity", "Количество гостей", PropertyTypeEnum.Integer);
        propertyService.createType("order_transport_cost", "Стоимость заказа", PropertyTypeEnum.Double);
        propertyService.createType("order_transport_price", "Стоимость за час", PropertyTypeEnum.Double);

        propertyService.createType("order_customer_fio", "Имя", PropertyTypeEnum.String);
        propertyService.createType("order_customer_phone", "Сотовый", PropertyTypeEnum.Phone);

        propertyService.createType("order_driver_fio", "Имя", PropertyTypeEnum.String);
        propertyService.createType("order_driver_phone", "Сотовый", PropertyTypeEnum.Phone);
    }
}
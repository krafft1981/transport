package com.rental.transport.service;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Request;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.RequestEntity;
import com.rental.transport.entity.RequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.RequestStatusEnum;
import com.rental.transport.mapper.RequestMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

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
    private RequestMapper requestMapper;

    @Scheduled(cron = "0 0 * * * *")
    public void setRequestExpired() {

        requestRepository.setExpired();
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

    @Transactional
    public List<Request> confirmRequest(String account, Long requestId)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        CustomerEntity driver = customerService.getEntity(account);
        RequestEntity request = requestRepository
                .findById(requestId)
                .orElseThrow(() -> new ObjectNotFoundException("Event", requestId));

        if (!request.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Confirmation");

        if (request.getStatus() != RequestStatusEnum.NEW)
            throw new IllegalArgumentException("На этот запрос уже ответили");

        CustomerEntity customer = request.getCustomer();
        TransportEntity transport = request.getTransport();
        ParkingEntity parking = transport.getParking().iterator().next();

        // validate future
        Date now = new Date();

        if ((request.getDay() < now.getTime()))
            throw new IllegalArgumentException("Запрос устарел");

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
                .findNewByTransportAndDay(transport.getId(), request.getDay())
                .stream()
                .forEach(entity -> {
                });

        requestRepository
                .findByCustomerAndDay(transport.getId(), request.getDay())
                .stream()
                .forEach(entity -> {
                });

        requestRepository
                .findByDriverAndDay(transport.getId(), request.getDay())
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

        CustomerEntity driver = customerService.getEntity(account);

        if (!request.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Нельзя отвечать на запрос другому водителю");

        if (request.getStatus() != RequestStatusEnum.NEW)
            throw new IllegalArgumentException("Запрос устарел");

        setInteracted(request, null, null);
        return getRequestAsDriver(driver);
    }

    @Transactional
    public Map<Integer, Event> createRequest(String account, Long transportId, Long day, Integer[] hours)
            throws ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);

        Date now = new Date();
        day = calendarService.getDayId(day);

        if (day < calendarService.getDayId(now.getTime()))
            throw new IllegalArgumentException("Запрос устарел");

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

    public Map<Integer, Event> getTransportCalendar(String account, Long day, Long transportId) throws ObjectNotFoundException {

        TransportEntity transport = transportService.getEntity(transportId);
        CustomerEntity customer = customerService.getEntity(account);
        return calendarService.getTransportCalendar(customer, transport, day);
    }

    public List<Request> getRequestAsCustomer(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return requestRepository
                .findNewByCustomer(customer.getId())
                .stream()
                .map(entity -> requestMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Request> getRequestAsDriver(CustomerEntity driver) {

        return requestRepository
                .findByDriver(driver.getId())
                .stream()
                .map(entity -> requestMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Request> getRequestAsDriver(String account) throws ObjectNotFoundException {

        CustomerEntity driver = customerService.getEntity(account);
        return getRequestAsDriver(driver);
    }
}

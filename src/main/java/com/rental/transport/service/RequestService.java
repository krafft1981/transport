package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.dto.Event;
import com.rental.transport.dto.Request;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.RequestEntity;
import com.rental.transport.entity.RequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.CalendarTypeEnum;
import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.enums.RequestStatusEnum;
import com.rental.transport.mapper.RequestMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
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
    private NotifyService notifyService;

    @Autowired
    private RequestMapper requestMapper;

    @Scheduled(cron = "0 0 * * * *")
    public void setRequestExpired() {

        requestRepository.setExpiredByDay();
    }

    public RequestEntity getEntity(Long id) throws ObjectNotFoundException {

        return requestRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Request", id));
    }

    private void setInteracted(RequestEntity request, CustomerEntity driver, Long orderId) {

        requestRepository
                .findToUpdateNewByDay(
                        request.getCustomer().getId(),
                        request.getTransport().getId(),
                        request.getDay()
                )
                .stream()
                .forEach(entity -> {
                    if (Objects.nonNull(driver) && entity.getDriver().equals(driver)) {
                        entity.setOrder(orderId);
                        entity.setInteract(RequestStatusEnum.ACCEPTED);
                        notifyService.requestConfirmed(request);
                    } else {
                        entity.setInteract(RequestStatusEnum.REJECTED);
                        notifyService.requestRejected(request);
                    }
                });

        requestRepository.save(request);
    }

    public List<Request> getRequest(String account, Long[] ids) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return StreamSupport.stream(requestRepository
                .findAllById(Arrays.asList(ids)).spliterator(), true)
                .map(entity -> requestMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Event> createRequest(String account, Long transportId, Long day, Integer[] hours)
            throws ObjectNotFoundException, IllegalArgumentException {

        final Long selectedDay = calendarService.getDayIdByTime(day);
        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);

        requestRepository
                .findByCustomerAndTransportAndDayAndStatus(customer, transport, selectedDay, RequestStatusEnum.NEW)
                .stream()
                .forEach(entity ->
                        transport
                                .getCustomer()
                                .stream()
                                .forEach(driverEntity -> notifyService.requestCanceled(driverEntity, customer, transport, selectedDay, hours))
                );

        requestRepository.deleteByCustomerAndTransportByDay(customer.getId(), transport.getId(), selectedDay);

        if (transport.getParking().isEmpty())
            throw new IllegalArgumentException("Данный транспорт не имеет стоянки");

        if (transport.getCustomer().isEmpty())
            throw new IllegalArgumentException("Данный транспорт не имеет водителей");

        if (Objects.nonNull(hours)) {
            calendarService.obsolescenceСheck(selectedDay, customer, hours);
            calendarService.sequenceСheck(hours);
            Integer minTime = Integer.parseInt(propertyService.getValue(transport.getProperty(), "transport_min_rent_time"));
            if (hours.length < minTime)
                throw new IllegalArgumentException("Выберите не менее чем " + minTime + " часа");

            calendarService.checkBusyByCustomer(customer, selectedDay, hours);
            calendarService.checkBusyByNote(customer, selectedDay, hours);

            Integer requestCount = 0;
            for (CustomerEntity driver : transport.getCustomer()) {
                try {
                    calendarService.checkBusyByCustomer(driver, selectedDay, hours);
                    calendarService.checkBusyByNote(driver, selectedDay, hours);
                    RequestEntity request = new RequestEntity(customer, driver, transport, selectedDay, hours);
                    requestRepository.save(request);
                    notifyService.requestCreated(request);
                    requestCount++;
                } catch (Exception e) {
                }
            }

            if (requestCount == 0)
                throw new IllegalArgumentException("Извините, некому принять заявку, попробуйте сделать заказ на другое время");
        }

        return calendarService.getTransportEvents(account, selectedDay, transportId);
    }

    @Transactional
    public List<Event> confirmRequest(String account, Long requestId)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        CustomerEntity driver = customerService.getEntity(account);
        RequestEntity request = getEntity(requestId);

        if (!request.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Подтверждение");

        if (request.getStatus() != RequestStatusEnum.NEW)
            throw new IllegalArgumentException("На этот запрос уже ответили");

        CustomerEntity customer = request.getCustomer();
        TransportEntity transport = request.getTransport();
        ParkingEntity parking = transport.getParking().iterator().next();

        calendarService.obsolescenceСheck(request.getDay(), customer, request.getHours());

        OrderEntity order = new OrderEntity(customer, transport, driver, request.getDay(), request.getHours());

        calendarService.checkBusyByCustomer(customer, request.getDay(), request.getHours());
        calendarService.checkBusyByNote(customer, request.getDay(), request.getHours());

        calendarService.checkBusyByCustomer(driver, request.getDay(), request.getHours());
        calendarService.checkBusyByNote(driver, request.getDay(), request.getHours());

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

        String customer_phone = propertyService.getValue(request.getCustomer().getProperty(), "customer_phone");
        String customer_fio = propertyService.getValue(request.getCustomer().getProperty(), "customer_fio");
        String driver_phone = propertyService.getValue(request.getDriver().getProperty(), "customer_phone");
        String driver_fio = propertyService.getValue(request.getDriver().getProperty(), "customer_fio");

        order.setCustomer(customer);
        order.setTransport(transport);
        order.setDay(request.getDay());
        order.setHours(request.getHours());
        order.setDriver(driver);

        orderRepository.save(order);

        calendarService.getEntity(
                request.getDay(),
                request.getHours(),
                CalendarTypeEnum.CUSTOMER,
                customer.getId(),
                order.getId(),
                customer_phone + " " + customer_fio
        );

        calendarService.getEntity(
                request.getDay(),
                request.getHours(),
                CalendarTypeEnum.CUSTOMER,
                driver.getId(),
                order.getId(),
                driver_phone + " " + driver_fio
        );

        request.setOrder(order.getId());

        setInteracted(request, driver, order.getId());

        rejectAllcrossRequests(request);
        return getRequestAsDriver(account);
    }

    @Transactional
    public List<Event> rejectRequest(String account, Long requestId)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        RequestEntity request = getEntity(requestId);
        CustomerEntity driver = customerService.getEntity(account);

        if (!request.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Нельзя отвечать на чужой запрос");

        if (request.getStatus() != RequestStatusEnum.NEW)
            throw new IllegalArgumentException("На этот запрос уже ответили");

        setInteracted(request, null, null);
        return getRequestAsDriver(account);
    }

    public List<Event> getRequestAsCustomer(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return requestRepository
                .findNewByCustomer(customer.getId())
                .stream()
                .map(entity -> {
                    Calendar calendar = new Calendar(entity.getDay(), entity.getHours());
                    return new Event(EventTypeEnum.REQUEST, calendar, entity.getId());
                })
                .collect(Collectors.toList());
    }

    public List<Event> getRequestAsDriver(String account) throws ObjectNotFoundException {

        CustomerEntity driver = customerService.getEntity(account);
        return requestRepository
                .findNewByDriver(driver.getId())
                .stream()
                .map(entity -> {
                    Calendar calendar = new Calendar(entity.getDay(), entity.getHours());
                    return new Event(EventTypeEnum.REQUEST, calendar, entity.getId());
                })
                .collect(Collectors.toList());
    }

    private void rejectAllcrossRequests(RequestEntity request) {

        requestRepository
                .findNewByCustomerAndDay(request.getCustomer().getId(), request.getDay())
                .stream()
                .forEach(entity -> {
                    for (Integer hour : request.getHours()) {
                        if (Arrays.asList(entity.getHours()).contains(hour)) {
                            rejectRequest(entity.getDriver().getAccount(), entity.getId());
                            break;
                        }
                    }
                });

        requestRepository
                .findNewByDriverAndDay(request.getDriver().getId(), request.getDay())
                .stream()
                .forEach(entity -> {
                    for (Integer hour : request.getHours()) {
                        if (Arrays.asList(entity.getHours()).contains(hour)) {
                            rejectRequest(entity.getDriver().getAccount(), entity.getId());
                            break;
                        }
                    }
                });
    }
}

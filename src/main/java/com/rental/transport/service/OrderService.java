package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.dto.Event;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.RequestEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.CalendarMapper;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.mapper.RequestMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.BooleanYesValidator;
import com.rental.transport.utils.validator.IStringValidator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
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
    private RequestService requestService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private CalendarMapper calendarMapper;

    @Autowired
    private RequestMapper requestMapper;

    public List<Event> getOrderByCustomer(String account, Pageable pageable)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return orderRepository
                .findByCustomer(customer, pageable)
                .stream()
                .map(entity ->
                        new Event(
                                orderMapper.toDto(entity),
                                calendarMapper.toDto(entity.getCalendar().iterator().next())
                        )
                )
                .collect(Collectors.toList());
    }

    public List<Event> getOrderByTransport(String account, Pageable pageable) {

        return customerService
                .getEntity(account)
                .getTransport()
                .stream()
                .map(transport -> orderRepository
                        .findByTransport(transport, pageable)
                        .stream()
                        .map(entity ->
                                new Event(
                                        orderMapper.toDto(entity),
                                        calendarMapper.toDto(entity.getCalendar().iterator().next())
                                )
                        )
                        .collect(Collectors.toList())
                )
                .flatMap(x -> x.stream())
                .collect(Collectors.toList());
    }

    public List<Event> getRequestAsCustomer(String account, Pageable pageable) {

        CustomerEntity customer = customerService.getEntity(account);
        return requestService
                .getByCustomer(customer, pageable)
                .stream()
                .map(entity -> new Event(
                        requestMapper.toDto(entity),
                        calendarMapper.toDto(entity.getCalendar()))
                )
                .collect(Collectors.toList());
    }

    public List<Event> getRequestAsDriver(String account, Pageable pageable) {

        CustomerEntity driver = customerService.getEntity(account);
        return requestService
                .getByDriver(driver, pageable)
                .stream()
                .map(entity -> new Event(
                        requestMapper.toDto(entity),
                        calendarMapper.toDto(entity.getCalendar()))
                )
                .collect(Collectors.toList());
    }

    public OrderEntity getEntity(Long id) throws ObjectNotFoundException {

        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));
    }

    @Transactional
    public Long putAbsentCustomerEntry(String account, Long day, Long start, Long stop)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        calendarService.checkCustomerBusy(customer, day, start, stop);
        CalendarEntity calendar = calendarService.getEntity(day, start, stop, true);
        customer.addCalendar(calendar);
        return calendar.getId();
    }

    @Transactional
    public void deleteAbsentCustomerEntry(String account, Long id)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        CalendarEntity calendar = calendarService.getEntity(id);
        customer.deleteCalendarEntity(calendar);
    }

    @Transactional
    public void createRequest(String account, Long transportId, Long day, Long start, Long stop)
            throws ObjectNotFoundException, IllegalArgumentException {

        TransportEntity transport = transportService.getEntity(transportId);

        if (transport.getParking().isEmpty())
            throw new IllegalArgumentException("Transport has't parking");

        CustomerEntity customer = customerService.getEntity(account);
        CalendarEntity calendar = calendarService.getEntity(day, start, stop, true);

        String minTime = propertyService.getValue(transport.getProperty(), "transport_min_rent_time");

        // validate duration
        Long interval = (calendar.getStopAt().getTime() - calendar.getStartAt().getTime()) / 1000;
        if (interval < Integer.parseInt(minTime) * 3600)
            throw new IllegalArgumentException("Wrong time interval");

        // validate future
        Date now = new Date();
        if (calendar.getStartAt().getTime() < now.getTime())
            throw new IllegalArgumentException("Allow request only in the future");

        calendarService.checkCustomerBusy(customer, day, start, stop);
        calendarService.checkTransportBusy(transport, day, start, stop);

        Boolean requestLost = true;
        for (CustomerEntity driver : transport.getCustomer()) {
            try {
                calendarService.checkCustomerBusy(
                        driver,
                        calendar.getDayNum(),
                        calendar.getStartAt().getTime(),
                        calendar.getStopAt().getTime()
                );
                requestService.putRequest(customer, driver, transport, calendar);
                requestLost = false;
            } catch (IllegalArgumentException e) {
            }
        }

        if (requestLost) {
            throw new IllegalArgumentException(
                    "No free drivers for: transport " + transportId +
                            " day: " + calendar.getDayNum() +
                            " start: " + calendar.getStartAt() +
                            " stop: " + calendar.getStopAt()
            );
        }
    }

    public List<Calendar> getTransportCalendar(String account, Long day, Long transportId) {

        return calendarService.getTransportCalendar(account, day, transportId);
    }

    public List<Event> getCustomerCalendarWithOrders(String account, Long day) {

        return calendarService.getCustomerCalendarWithOrders(account, day);
    }

    @Transactional
    public void confirmRequest(String account, Long requestId)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        CustomerEntity driver = customerService.getEntity(account);
        RequestEntity request = requestService.getEntity(requestId);

        if (!request.getTransport().getCustomer().contains(driver))
            throw new AccessDeniedException("Confirmation");

        if (request.getInteractAt() != null)
            throw new IllegalArgumentException("Reject with interacted");

        CustomerEntity customer = request.getCustomer();
        TransportEntity transport = request.getTransport();
        CalendarEntity calendar = request.getCalendar();
        ParkingEntity parking = transport.getParking().iterator().next();

        // validate future
        Date now = new Date();
        if (calendar.getStartAt().getTime() < now.getTime())
            throw new IllegalArgumentException("Allow orders only in the future");

        calendarService.checkCustomerBusy(
                driver,
                calendar.getDayNum(),
                calendar.getStartAt().getTime(),
                calendar.getStopAt().getTime()
        );

        calendarService.checkCustomerBusy(
                customer,
                calendar.getDayNum(),
                calendar.getStartAt().getTime(),
                calendar.getStopAt().getTime()
        );

        calendarService.checkTransportBusy(
                transport,
                calendar.getDayNum(),
                calendar.getStartAt().getTime(),
                calendar.getStopAt().getTime()
        );

        OrderEntity order = new OrderEntity();

        request.setOrder(order.getId());
        request.setInteract();

        String price = propertyService.getValue(transport.getProperty(), "transport_price");

        // calculate cost
        Long interval = (calendar.getStopAt().getTime() - calendar.getStartAt().getTime()) / 1000;
        double cost = Math.ceil(interval / 3600) * Double.parseDouble(price);

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
                propertyService.copy("order_transport_use_driver", transport.getProperty(), "transport_use_driver"),

                propertyService.create("order_transport_cost", String.format("%.2f", cost)),

                propertyService.copy("order_customer_fio", customer.getProperty(), "customer_fio"),
                propertyService.copy("order_customer_phone", customer.getProperty(), "customer_phone")
        );

        customer.addCalendar(calendar);
        transport.addCalendar(calendar);

        order.setCustomer(customer);
        order.setTransport(transport);
        order.addCalendar(calendar);
        order.addDriver(driver);

        String useDriver = propertyService.getValue(transport.getProperty(), "transport_use_driver");
        IStringValidator yesValidator = new BooleanYesValidator();
        if (yesValidator.validate(useDriver))
            driver.addCalendar(calendar);

        orderRepository.save(order);
    }

    @Transactional
    public void rejectRequest(String account, Long requestId)
            throws ObjectNotFoundException, AccessDeniedException, IllegalArgumentException {

        RequestEntity request = requestService.getEntity(requestId);
        CustomerEntity customer = customerService.getEntity(account);

        if (!request.getTransport().getCustomer().contains(customer) && !request.getCustomer().equals(customer))
            throw new AccessDeniedException("Confirmation");

        if (request.getInteractAt() != null)
            throw new IllegalArgumentException("Reject with interacted");

        customer = request.getCustomer();
        CustomerEntity driver = request.getDriver();
        CalendarEntity calendar = request.getCalendar();
        TransportEntity transport = request.getTransport();

        requestService.setInteract(request);

        driver.deleteCalendarEntity(calendar);
        customer.deleteCalendarEntity(calendar);
        transport.deleteCalendarEntity(calendar);
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
        propertyService.createType("order_transport_use_driver", "Используется с водителем", PropertyTypeEnum.Boolean);

        propertyService.createType("order_customer_fio", "Имя", PropertyTypeEnum.String);
        propertyService.createType("order_customer_phone", "Сотовый", PropertyTypeEnum.Phone);

        propertyService.createType("order_driver_fio", "Имя", PropertyTypeEnum.String);
        propertyService.createType("order_driver_phone", "Сотовый", PropertyTypeEnum.Phone);
    }
}
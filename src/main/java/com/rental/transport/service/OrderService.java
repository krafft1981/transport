package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.OrderStatusEnum;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.BooleanYesValidator;
import com.rental.transport.utils.validator.IStringValidator;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import lombok.NonNull;
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
    private ConfirmationService confirmationService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Transactional
    public void confirmOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerService.getEntity(account);
        OrderEntity order = getEntity(orderId);

        if (!order.getStatus().equals(OrderStatusEnum.New))
            throw new IllegalArgumentException("Order has status not New");

        if (order.getTransport().getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Confirmation");

        order.setConfirmedAt(new Date());
        TransportEntity transport = order.getTransport();
        CalendarEntity calendar = order.getCalendar().iterator().next();

        calendarService.checkCustomerBusy(
                driver,
                calendar.getDayNum(),
                calendar.getStartAt().getTime(),
                calendar.getStopAt().getTime()
        );

        calendarService.checkTransportBusy(
                order.getTransport(),
                calendar.getDayNum(),
                calendar.getStartAt().getTime(),
                calendar.getStopAt().getTime()
        );

        String useDriver = propertyService.getValue(transport.getProperty(), "transport_use_driver");
        IStringValidator yesValidator = new BooleanYesValidator();
        if (yesValidator.validate(useDriver))
            driver.addCalendar(calendar);

//            confirmationService.interaction(driver, order);
//            confirmationService.deleteByOrderId(order.getId());

        transport.addCalendar(calendar);

        order.addDriver(driver);
        order.addProperty(
                propertyService.copy("order_driver_fio", driver.getProperty(), "customer_fio"),
                propertyService.copy("order_driver_phone", driver.getProperty(), "customer_phone")
        );

        // TODO rejectOrder все заказы с пересекающимся временем
        // TODO Нужно поискать за этот день все пересекающиеся деапазоны для этого транспорта и отменить их

//        confirmationService
//                .getByCustomer(customer)
//                .stream()
//                .map(entity -> orderMapper.toDto(entity))
//                .collect(Collectors.toList());

        order.setStatus(OrderStatusEnum.Confirmed);
    }

    @Transactional
    public void rejectOrder(String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerService.getEntity(account);
        OrderEntity order = getEntity(orderId);

        if (!order.getStatus().equals(OrderStatusEnum.New))
            throw new IllegalArgumentException("Order has status not New");

        if (order.getTransport().getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Confirmation");

        CalendarEntity calendar = order.getCalendar().iterator().next();
        order.getCustomer().deleteCalendarEntity(calendar);

//        confirmationService.interaction(driver, order);
        order.setStatus(OrderStatusEnum.Rejected);
    }

    @Transactional
    public Long create(@NonNull String account, Long transportId, Long day, Long start, Long stop)
            throws ObjectNotFoundException, IllegalArgumentException {

        OrderEntity order = new OrderEntity();

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);

        if (transport.getParking().isEmpty())
            throw new IllegalArgumentException("Transport has't parking");

        ParkingEntity parking = transport.getParking().iterator().next();
        CalendarEntity calendar = calendarService.getEntity(day, start, stop, true);

        order.setCustomer(customer);
        order.setTransport(transport);
        order.addCalendar(calendar);

        String price = propertyService.getValue(transport.getProperty(), "transport_price");
        String minTime = propertyService.getValue(transport.getProperty(), "transport_min_rent_time");

        // validate future
        Date now = new Date();
        if (start < now.getTime())
            throw new IllegalArgumentException("Allow orders only in the future");

        // validate duration
        Long interval = (calendar.getStopAt().getTime() - calendar.getStartAt().getTime()) / 1000;
        if (interval < Integer.parseInt(minTime) * 3600)
            throw new IllegalArgumentException("Wrong time interval");

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

                propertyService.create("order_transport_cost", String.format("%f.02", cost)),

                propertyService.copy("order_customer_fio", customer.getProperty(), "customer_fio"),
                propertyService.copy("order_customer_phone", customer.getProperty(), "customer_phone")
        );

//        confirmationService.putOrder(order);

        calendarService.checkCustomerBusy(customer, day, start, stop);
        calendarService.checkTransportBusy(transport, day, start, stop);

        customer.addCalendar(calendar);
        order.setStatus(OrderStatusEnum.Confirmed);

        return orderRepository.save(order).getId();
    }

    public List<Order> getOrderByConfirmation(String account, Pageable pageable) {

        CustomerEntity customer = customerService.getEntity(account);
        return confirmationService
                .getByCustomer(customer, pageable)
                .stream()
                .map(entity -> orderMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Order> getOrderByCustomer(String account, Pageable pageable)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return orderRepository
                .findByCustomer(customer, pageable)
                .stream()
                .map(order -> orderMapper.toDto(order))
                .collect(Collectors.toList());
    }

    public List<Order> getOrderByTransport(String account, Pageable pageable) {

        List<Order> orderList = new ArrayList();
        customerService
                .getEntity(account)
                .getTransport()
                .stream()
                .forEach(transport -> {
                    orderRepository
                            .findByTransport(transport, pageable)
                            .stream()
                            .forEach(order -> {
                                orderList.add(orderMapper.toDto(order));
                            });
                });
        return orderList;
    }

    public OrderEntity getEntity(Long id) throws ObjectNotFoundException {

        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));
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
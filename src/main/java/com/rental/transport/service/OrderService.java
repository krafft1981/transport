package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.MessageEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.entity.PropertyTypeEntity;
import com.rental.transport.entity.PropertyTypeRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.OrderStatusEnum;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.utils.exceptions.AccessDeniedException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
    private OrderMapper orderMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PropertyTypeRepository propertyTypeRepository;

    public void sendMessage(String account, Long orderId, String message)
            throws ObjectNotFoundException {

        OrderEntity order = getEntity(orderId);
        CustomerEntity customer = customerService.getEntity(account);
        MessageEntity entity = new MessageEntity(customer, message);
        order.addMessage(entity);
    }

    @Transactional
    public void confirmOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerService.getEntity(account);
        OrderEntity order = getEntity(orderId);

        if (!order.getStatus().equals(OrderStatusEnum.New))
            throw new IllegalArgumentException("Order has status not New");

        if (order.getTransport().getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Confirmation");

        confirmationService.interaction(driver, order);
        confirmationService.deleteByOrderId(order.getId());

        order.addDriver(driver);
        order.addProperty(
                copyProperty("order_driver_fio", driver.getProperty(), "customer_fio"),
                copyProperty("order_driver_phone", driver.getProperty(), "customer_phone")
        );

        order.setStatus(OrderStatusEnum.Confirmed);
    }

    @Transactional
    public void rejectOrder(@NonNull String account, Long orderId)
            throws ObjectNotFoundException, AccessDeniedException {

        CustomerEntity driver = customerService.getEntity(account);
        OrderEntity order = getEntity(orderId);

        if (!order.getStatus().equals(OrderStatusEnum.New))
            throw new IllegalArgumentException("Order has status not New");

        if (order.getTransport().getCustomer().contains(driver) == false)
            throw new AccessDeniedException("Confirmation");

        confirmationService.interaction(driver, order);
        order.setStatus(OrderStatusEnum.Rejected);
        //free people and transport time
    }

    public PropertyEntity copyProperty(String newType, Set<PropertyEntity> entryes, String oldType)
            throws ObjectNotFoundException {

        PropertyTypeEntity pNew = propertyTypeRepository.findByLogicName(newType);
        if (Objects.isNull(pNew))
            throw new ObjectNotFoundException("PropertyType", newType);

        PropertyTypeEntity pOld = propertyTypeRepository.findByLogicName(oldType);
        if (Objects.isNull(pOld))
            throw new ObjectNotFoundException("PropertyType", oldType);

        PropertyEntity entity = propertyService.searchProperty(entryes, oldType);
        return propertyService.create(newType, entity.getValue());
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

        // validate duration
        Long interval = calendar.getStopAt().getTime() - calendar.getStartAt().getTime();
        if (interval < Integer.parseInt(minTime) * 3600)
            throw new IllegalArgumentException("Wrong time interval");

        // calculate cost
        double cost = Math.ceil(interval / 3600) * Double.parseDouble(price);

        order.addProperty(
                copyProperty("order_parking_name", parking.getProperty(), "parking_name"),
                copyProperty("order_parking_latitude", parking.getProperty(), "parking_latitude"),
                copyProperty("order_parking_longitude", parking.getProperty(), "parking_longitude"),
                copyProperty("order_parking_address", parking.getProperty(), "parking_address"),
                copyProperty("order_parking_locality", parking.getProperty(), "parking_locality"),
                copyProperty("order_parking_region", parking.getProperty(), "parking_region"),

                copyProperty("order_transport_name", transport.getProperty(), "transport_name"),
                copyProperty("order_transport_capacity", transport.getProperty(), "transport_capacity"),
                copyProperty("order_transport_price", transport.getProperty(), "transport_price"),
                copyProperty("order_transport_use_driver", transport.getProperty(), "transport_use_driver"),

                propertyService.create("order_transport_cost", String.format("%f02", cost)),

                copyProperty("order_customer_fio", customer.getProperty(), "customer_fio"),
                copyProperty("order_customer_phone", customer.getProperty(), "customer_phone")
        );

        confirmationService.putOrder(order);

        calendarService.checkCustomerBusy(customer, day, start, stop);
        calendarService.checkTransportBusy(transport, day, start, stop);

        customer.addCalendar(calendar);
        transport.addCalendar(calendar);

        return orderRepository.save(order).getId();
    }

    public List<Order> getOrderListByConfirmation(String account, Pageable pageable) {

        CustomerEntity customer = customerService.getEntity(account);
        return confirmationService
                .getByCustomer(customer, pageable)
                .stream()
                .map(entity -> {
                    return orderMapper.toDto(entity);
                })
                .collect(Collectors.toList());
    }

    public List<Order> getOrderListByCustomer(String account, Pageable pageable)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return orderRepository
                .findByCustomer(customer, pageable)
                .stream()
                .map(order -> {
                    return orderMapper.toDto(order);
                })
                .collect(Collectors.toList());
    }

    public List<Order> getOrderListByTransport(String account, Pageable pageable) {

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

    public Order getDto(Long id) throws ObjectNotFoundException {

        return orderMapper.toDto(getEntity(id));
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
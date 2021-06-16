package com.rental.transport.service;

import com.rental.transport.dto.Order;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.PropertyTypeEnum;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    public List<Order> getOrderByDriver(String account) throws ObjectNotFoundException {

        CustomerEntity driver = customerService.getEntity(account);
        return orderRepository
                .findByDriver(driver.getId())
                .stream()
                .map(entity -> orderMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Order> getOrderByCustomer(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return orderRepository
                .findByCustomer(customer.getId())
                .stream()
                .map(entity -> orderMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Order> getOrderByMyTransport(String account) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        List<Order> result = new ArrayList();
        for (TransportEntity transport : customer.getTransport()) {
            orderRepository
                    .findByTransport(transport.getId())
                    .stream()
                    .forEach(entity -> result.add(orderMapper.toDto(entity)));
        }

        return result;
    }

    public OrderEntity getEntity(Long id) throws ObjectNotFoundException {

        return orderRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Order", id));
    }

    public Order getOrder(String account, Long id) throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        OrderEntity entity = getEntity(id);
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

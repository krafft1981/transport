package com.rental.transport.controller;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Order;
import com.rental.transport.dto.Text;
import com.rental.transport.service.OrderService;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService service;

    @ApiOperation(
            value = "Получение списка заказов по транспорту"
    )
    @GetMapping(value = "/transport")
    public List<Order> doGetOrderByTransport(Principal principal) {
        return service.getOrderByMyTransport(principal.getName());
    }

    @ApiOperation(
            value = "Получение списка заказов по заказчику"
    )
    @GetMapping(value = "/customer")
    public List<Order> doGetOrderByCustomer(Principal principal) {
        return service.getOrderByCustomer(principal.getName());
    }

    @ApiOperation(
            value = "Получение списка заказов по водителю"
    )
    @GetMapping(value = "/driver")
    public List<Order> doGetOrderByDriver(Principal principal) {
        return service.getOrderByDriver(principal.getName());
    }

    @ApiOperation(
            value = "Редактивание записи в записной книге"
    )
    @PutMapping(value = "/absent")
    public Long doPutAbsentCustomer(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id,
            @RequestBody Text body) {

        return 0L;//service.putAbsentCustomerEntry(principal.getName(), id, body);
    }

    @ApiOperation(
            value = "Создание записи в записной книге"
    )
    @PostMapping(value = "/absent")
    public Event doPostAbsentCustomer(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "hour", required = true) Integer[] hours) {

        return null;//service.postAbsentCustomerEntry(principal.getName(), day, hours);
    }

    @ApiOperation(
            value = "Удаление записи из записной книги"
    )
    @DeleteMapping(value = "/absent")
    public void doDeleteAbsentCustomer(
            Principal principal,
            @RequestParam(value = "id", required = true) Long[] ids) {
        //service.deleteAbsentCustomerEntry(principal.getName(), ids);
    }

    @ApiOperation(
            value = "Получение календаря по транспорту"
    )
    @GetMapping(value = "/calendar/transport")
    public Map<Integer, Event> doGetTransportCalendar(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "transport_id", required = true) Long transportId) {

        return null;
//        return service.getTransportCalendar(principal.getName(), day, transportId);
    }

    @ApiOperation(
            value = "Получение календаря по пользователю"
    )
    @GetMapping(value = "/calendar/customer")
    public Map<Integer, Event> doGetCustomerCalendar(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day) {

        return null;
//        return service.getCustomerCalendar(principal.getName(), day);
    }

    @ApiOperation(
            value = "Добавление сообщения к заказу"
    )
    @PostMapping(value = "/message", consumes = "application/json", produces = "application/json")
    public Order doPostCustomerMessage(
            Principal principal,
            @RequestParam(value = "order_id", required = true) Long orderId,
            @RequestBody Text body) {

        return null;
//        return service.postOrderMessage(principal.getName(), orderId, body);
    }

    @ApiOperation(
            value = "Получение заказа по Id"
    )
    @GetMapping
    public Order doGetOrder(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

        return service.getOrder(principal.getName(), id);
    }
}

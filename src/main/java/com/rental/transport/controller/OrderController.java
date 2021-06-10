package com.rental.transport.controller;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Order;
import com.rental.transport.dto.Request;
import com.rental.transport.service.OrderService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RequestMapping(value = "/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService service;

    @ApiOperation(
            value = "Подтверждение в возможности выполнения заказа"
    )
    @PostMapping(value = "/request/confirm")
    public List<Request> doPostConfirmOrder(
            Principal principal,
            @RequestParam(value = "request_id", required = true) Long requestId) {

        return service.confirmRequest(principal.getName(), requestId);
    }

    @ApiOperation(
            value = "Отказ в возможности выполнения заказа"
    )
    @PostMapping(value = "/request/reject")
    public List<Request> doPostRejectOrder(
            Principal principal,
            @RequestParam(value = "request_id", required = true) Long requestId) {

        return service.rejectRequest(principal.getName(), requestId);
    }

    @ApiOperation(
            value = "Создание запроса на заказ"
    )
    @PostMapping(value = "/request")
    public Map<Integer, Event> doPost(
            Principal principal,
            @RequestParam(value = "transport_id", required = true) Long transport_id,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "hour", required = false) Integer[] hour) {

        return service.createRequest(principal.getName(), transport_id, day, hour);
    }

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
            value = "Получение списка запросов заказчика"
    )
    @GetMapping(value = "/request/customer")
    public List<Request> doGetRequestAsCustomer(Principal principal) {
        return service.getRequestAsCustomer(principal.getName());
    }

    @ApiOperation(
            value = "Получение списка запросов водителя"
    )
    @GetMapping(value = "/request/driver")
    public List<Request> doGetRequestAsDriver(Principal principal) {
        return service.getRequestAsDriver(principal.getName());
    }

    @ApiOperation(
            value = "Редактивание записи в записной книге"
    )
    @PutMapping(value = "/absent")
    public Long doPutAbsentCustomer(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id,
            @RequestBody String message) {

        return service.putAbsentCustomerEntry(principal.getName(), id, message);
    }

    @ApiOperation(
            value = "Создание записи в записной книге"
    )
    @PostMapping(value = "/absent")
    public Event doPostAbsentCustomer(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "hour", required = true) Integer[] hours) {

        return service.postAbsentCustomerEntry(principal.getName(), day, hours);
    }

    @ApiOperation(
            value = "Удаление записи из записной книги"
    )
    @DeleteMapping(value = "/absent")
    public void doDeleteAbsentCustomer(
            Principal principal,
            @RequestParam(value = "id", required = true) Long[] ids) {
        service.deleteAbsentCustomerEntry(principal.getName(), ids);
    }

    @ApiOperation(
            value = "Получение календаря по транспорту"
    )
    @GetMapping(value = "/calendar/transport")
    public Map<Integer, Event> doGetTransportCalendar(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "transport_id", required = true) Long transportId) {

        return service.getTransportCalendar(principal.getName(), day, transportId);
    }

    @ApiOperation(
            value = "Получение календаря по пользователю"
    )
    @GetMapping(value = "/calendar/customer")
    public Map<Integer, Event> doGetCustomerCalendar(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day) {

        return service.getCustomerCalendar(principal.getName(), day);
    }

    @ApiOperation(
            value = "Добавление сообщения к заказу"
    )
    @PostMapping(value = "/message", consumes = "text/plain", produces = "application/json")
    public Order doPostCustomerMessage(
            Principal principal,
            @RequestParam(value = "order_id", required = true) Long orderId,
            @RequestBody String message) {

        return service.postOrderMessage(principal.getName(), orderId, message);
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

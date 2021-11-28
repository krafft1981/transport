package com.rental.transport.controller;

import com.rental.transport.dto.Order;
import com.rental.transport.dto.Text;
import com.rental.transport.service.OrderService;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @ApiOperation(
            value = "Получение списка заказов одобренных клиенту"
    )
    @GetMapping(value = "/customer")
    public List<Order> doGetOrderByCustomer(Principal principal) {
        return orderService.getOrderByCustomer(principal.getName());
    }

    @ApiOperation(
            value = "Получение списка заказов назначенных водителю"
    )
    @GetMapping(value = "/driver")
    public List<Order> doGetOrderByDriver(Principal principal) {
        return orderService.getOrderByDriver(principal.getName());
    }

    @ApiOperation(
            value = "Получение заказа по Id"
    )
    @GetMapping
    public Order doGetOrder(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

        return orderService.getOrder(principal.getName(), id);
    }

    @ApiOperation(
            value = "Добавление сообщения к заказу"
    )
    @PostMapping(
            value = "/message",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Order doPostCustomerMessage(
            Principal principal,
            @RequestParam(value = "order_id", required = true) Long orderId,
            @RequestBody Text body) {

        return orderService.postOrderMessage(principal.getName(), orderId, body);
    }
}

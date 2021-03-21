package com.rental.transport.controller;

import com.rental.transport.service.OrderService;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping(value = "/confirm")
    public void doPostConfirmOrderRequest(
            Principal principal,
            @RequestParam(value = "order_id", required = true) Long orderId) {

        service.confirmOrder(principal.getName(), orderId);
    }

    @PostMapping(value = "/reject")
    public void doPostRejectOrderRequest(
            Principal principal,
            @RequestParam(value = "order_id", required = true) Long orderId) {

        service.rejectOrder(principal.getName(), orderId);
    }

    @PostMapping
    public Long doPostOrderRequest(
            Principal principal,
            @RequestParam(value = "transport_id", required = true) Long transport_id,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "start", required = true) Long start,
            @RequestParam(value = "stop", required = true) Long stop) {

        return service.create(principal.getName(), transport_id, day, start, stop);
    }
}

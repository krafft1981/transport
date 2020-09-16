package com.rental.transport.controller;

import com.rental.transport.dto.Order;
import com.rental.transport.service.OrderService;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService service;

    @PostMapping
    public Long doPostOrderRequest(
            Principal principal,
            @RequestParam(value = "transport_id", required = true) Long transport_id,
            @RequestParam(value = "start", required = true) Integer start,
            @RequestParam(value = "stop", required = true) Integer stop) {

        return service.create(
                principal.getName(),
                transport_id,
                new Date((long) start * 1000),
                new Date((long) stop * 1000));
    }

    @GetMapping(value = "/page")
    public List<Order> doGetPagesOrderRequest(
            Principal principal,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getByPage(principal.getName(), pageable);
    }

    @GetMapping(value = "/time")
    public List<Order> doGetTimesOrderRequest(
            Principal principal,
            @RequestParam(value = "transport", required = true) Long transportId,
            @RequestParam(value = "start", required = true) Integer start,
            @RequestParam(value = "stop", required = true) Integer stop) {

        return service.getByTime(transportId,
                new Date((long) start * 1000),
                new Date((long) stop * 1000));
    }

    @GetMapping(value = "/request")
    public List<Long> doGetOrderRequest(
            Principal principal) {

        return service.getOrderRequestList(principal.getName());
    }

    @PostMapping(value = "/confirm")
    public void doPostConfirmOrderRequest(
            Principal principal,
            @RequestParam(value = "orderId", required = true) Long orderId) {

        service.confirmOrder(principal.getName(), orderId);
    }

    @PostMapping(value = "/reject")
    public void doPostRejectOrderRequest(
            Principal principal,
            @RequestParam(value = "orderId", required = true) Long orderId) {

        service.rejectOrder(principal.getName(), orderId);
    }
}

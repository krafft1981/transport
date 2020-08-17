package com.rental.transport.controller;

import com.rental.transport.dto.Order;
import com.rental.transport.service.OrderService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/order")
@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping
    public Order doGetOrderRequest (
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

        return orderService.findById(principal.getName(), id);
    }

    @GetMapping(value = "/list")
    public List<Order> doGetOrderListRequest(
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {
        return orderService.getList(page, size);
    }

    @PutMapping
    public void doPutOrderRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

    }

    @PostMapping
    public Long doPostOrderRequest(
            Principal principal,
            @RequestParam(value = "order", required = true) Order order) {

        return orderService.newOrder(principal.getName());
    }

    @DeleteMapping
    public void doDeleteOrderRequest(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id) {

    }
}

package com.rental.transport.controller;

import com.rental.transport.dto.Order;
import com.rental.transport.service.OrderService;
import java.security.Principal;
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
    public Long goPostOrderRequest(
            Principal principal,
            @RequestParam(value = "transport_id", required = true) Long transport_id) {

        return service.create(principal.getName(), transport_id);
    }

    @GetMapping(value = "/list")
    public List<Order> goGetPagesOrderRequest(
            Principal principal,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(principal.getName(), pageable);
    }

    @GetMapping(value = "/count")
    public Long doGetCountRequest() {

        return service.count();
    }
}

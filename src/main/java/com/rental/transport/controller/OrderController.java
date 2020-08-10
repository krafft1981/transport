package com.rental.transport.controller;

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

    @GetMapping
    public void doGetOrderRequest (
            @RequestParam(value = "id", required = false) Long id) {
    }

    @PutMapping
    public void doPutOrderRequest(
            @RequestParam(value = "id", required = true) Long id) {
    }

    @PostMapping
    public void doPostOrderRequest() {
    }

    @DeleteMapping
    public void doDeleteOrderRequest(
            @RequestParam(value = "id", required = true) Long id) {
    }
}

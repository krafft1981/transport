package com.rental.transport.controller;

import com.rental.transport.dto.Calendar;
import com.rental.transport.dto.Event;
import com.rental.transport.dto.Order;
import com.rental.transport.service.CalendarService;
import java.security.Principal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/calendar")
@RestController
public class CalendarController {

    @Autowired
    private CalendarService service;

    @GetMapping(value = "/transport")
    public List<Calendar> doGetTransportCalendarRequest(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "transport_id", required = true) Long transportId) {

        return service.getTransportCalendar(principal.getName(), day, transportId);
    }

    @GetMapping(value = "/customer")
    public List<Event> doGetCustomerCalendarRequest(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day) {

        return service.getCustomerCalendarWithOrders(principal.getName(), day);
    }

    @PutMapping
    public Long doPutAbsentCustomerRequest(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "start", required = true) Long start,
            @RequestParam(value = "stop", required = true) Long stop) {

        return service.putAbsentCustomerEntry(principal.getName(), day, start, stop);
    }

    @DeleteMapping
    public void doDeleteAbsentCustomerRequest(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "start", required = true) Long start,
            @RequestParam(value = "stop", required = true) Long stop) {

        service.deleteAbsentCustomerEntry(principal.getName(), day, start, stop);
    }

    @GetMapping(value = "/transport")
    public List<Order> doGetOrderByTransport(
            Principal principal,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getOrderListByTransport(principal.getName(), pageable);
    }

    @GetMapping(value = "/customer")
    public List<Order> doGetOrderByCustomer(
            Principal principal,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getOrderListByCustomer(principal.getName(), pageable);
    }

    @GetMapping(value = "/confirmation")
    public List<Order> doGetForConfirmation(
            Principal principal,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getOrderListByConfirmation(principal.getName(), pageable);
    }
}

package com.rental.transport.controller;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Order;
import com.rental.transport.dto.Request;
import com.rental.transport.service.OrderService;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @PostMapping(value = "/request/confirm")
    public void doPostConfirmOrder(
            Principal principal,
            @RequestParam(value = "request_id", required = true) Long requestId) {

        service.confirmRequest(principal.getName(), requestId);
    }

    @PostMapping(value = "/request/reject")
    public void doPostRejectOrder(
            Principal principal,
            @RequestParam(value = "request_id", required = true) Long requestId) {

        service.rejectRequest(principal.getName(), requestId);
    }

    @PostMapping
    public Map<Integer, Event> doPost(
            Principal principal,
            @RequestParam(value = "transport_id", required = true) Long transport_id,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "hour", required = false) Integer[] hour) {

        return service.createRequest(principal.getName(), transport_id, day, hour);
    }

    @GetMapping
    public List<Order> doGet(Principal principal) {
        return service.getOrder(principal.getName());
    }

    @GetMapping(value = "/transport")
    public Map<Integer, Event> doGetOrderByTransport(
            Principal principal,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getOrderByMyTransport(principal.getName(), pageable);
    }

    @GetMapping(value = "/customer")
    public Map<Integer, Event> doGetOrderByCustomer(
            Principal principal,
            @RequestParam(value = "page", required = true) Integer page,
            @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getOrderByCustomer(principal.getName(), pageable);
    }

    @GetMapping(value = "/request/customer")
    public List<Request> doGetRequestAsCustomer(
            Principal principal) {

        return service.getRequestAsCustomer(principal.getName());
    }

    @GetMapping(value = "/request/driver")
    public List<Request> doGetRequestAsDriver(
            Principal principal) {

        return service.getRequestAsDriver(principal.getName());
    }

    @PutMapping(value = "/absent")
    public Long doPutAbsentCustomer(
            Principal principal,
            @RequestParam(value = "id", required = true) Long id,
            @RequestBody String message) {

        return service.putAbsentCustomerEntry(principal.getName(), id, message);
    }

    @PostMapping(value = "/absent")
    public Event doPostAbsentCustomer(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "hour", required = true) Integer[] hours) {

        return service.postAbsentCustomerEntry(principal.getName(), day, hours);
    }

    @DeleteMapping(value = "/absent")
    public void doDeleteAbsentCustomer(
            Principal principal,
            @RequestParam(value = "id", required = true) Long[] ids) {
        service.deleteAbsentCustomerEntry(principal.getName(), ids);
    }

    @GetMapping(value = "/calendar/transport")
    public Map<Integer, Event> doGetTransportCalendar(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "transport_id", required = true) Long transportId) {

        return service.getTransportCalendar(principal.getName(), day, transportId);
    }

    @GetMapping(value = "/calendar/customer")
    public Map<Integer, Event> doGetCustomerCalendar(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day) {

        return service.getCustomerCalendarWithOrders(principal.getName(), day);
    }
}

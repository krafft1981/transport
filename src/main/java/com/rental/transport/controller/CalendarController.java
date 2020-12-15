package com.rental.transport.controller;

import com.rental.transport.dto.Calendar;
import com.rental.transport.service.CalendarService;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value="/calendar")
@RestController
public class CalendarController {

    @Autowired
    private CalendarService service;

    @GetMapping(value = "/count")
    public Long doGetCountRequest() {

        return service.count();
    }

    @GetMapping(value = "/transport")
    public List<Calendar> doGetTransportCalendarRequest(
            Principal principal,
            @RequestParam(value = "days", required = true) Long[] days,
            @RequestParam(value = "transport_id", required = true) Long id) {

        return service.getTransportCalendar(id, days);
    }

    @GetMapping(value = "/customer")
    public List<Calendar> doGetCustomerCalendarRequest(
            Principal principal,
            @RequestParam(value = "days", required = true) Long[] days) {

        return service.getCustomerCalendar(principal.getName(), days);
    }

    @PutMapping
    public void doPutOutRequest(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "start", required = true) Long start,
            @RequestParam(value = "stop", required = true) Long stop) {

        service.putBusy(
                principal.getName(),
                day,
                new Date(start),
                new Date(stop)
        );
    }

    @DeleteMapping
    public void doDeleteOutRequest(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day,
            @RequestParam(value = "start", required = true) Long start,
            @RequestParam(value = "stop", required = true) Long stop) {

        service.deleteBusy(
                principal.getName(),
                day,
                new Date(start),
                new Date(stop));
    }
}

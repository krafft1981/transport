package com.rental.transport.controller;

import com.rental.transport.service.CalendarService;
import java.security.Principal;
import java.util.Date;
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

    @PutMapping
    public void doPutOutRequest(
            Principal principal,
            @RequestParam(value = "start", required = true) Integer start,
            @RequestParam(value = "stop", required = true) Integer stop) {

        service.putOutEvent(
                principal.getName(),
                new Date((long) start * 1000),
                new Date((long) stop * 1000));
    }

    @DeleteMapping
    public void doDeleteOutRequest(
            Principal principal,
            @RequestParam(value = "start", required = true) Integer start,
            @RequestParam(value = "stop", required = true) Integer stop) {

        service.deleteOutEvent(
                principal.getName(),
                new Date((long) start * 1000),
                new Date((long) stop * 1000));
    }

    @GetMapping
    public void doGetOutRequest(
            Principal principal,
            @RequestParam(value = "start", required = true) Integer start,
            @RequestParam(value = "stop", required = true) Integer stop) {

        service.getEventList(
                principal.getName(),
                new Date((long) start * 1000),
                new Date((long) stop * 1000));
    }
}

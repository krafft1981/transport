package com.rental.transport.controller;

import com.rental.transport.dto.Calendar;
import com.rental.transport.service.CalendarService;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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

    @GetMapping
    public List<Calendar> doGetRequest(
            Principal principal,
            @RequestParam(value = "start", required = true) String start,
            @RequestParam(value = "stop", required = true) String stop) {

        try {
            return service.getEvents(
                    principal.getName(),
                    new SimpleDateFormat("yyyy-MM-dd HH").parse(start),
                    new SimpleDateFormat("yyyy-MM-dd HH").parse(stop));
        }
        catch (Exception e) {

        }

        return null;
    }
}

package com.rental.transport.controller;

import com.rental.transport.dto.Calendar;
import com.rental.transport.dto.Event;
import com.rental.transport.dto.Text;
import com.rental.transport.service.CalendarService;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping(value = "/calendar")
@RestController
public class CalendarController {

    @Autowired
    private CalendarService calendarService;

    @ApiOperation(
            value = "Получение календаря заказчика транспорта"
    )
    @GetMapping(value = "/transport")
    public List<Event> doGetTransportCalendar(
            Principal principal,
            @RequestParam(value = "transport_id", required = true) Long transport_id,
            @RequestParam(value = "day", required = true) Long day) {

        return calendarService.getTransportEvents(principal.getName(), day, transport_id);
    }

    @ApiOperation(
            value = "Получение календаря владельца транспорта"
    )
    @GetMapping(value = "/customer")
    public List<Event> doGetCustomerCalendar(
            Principal principal,
            @RequestParam(value = "day", required = true) Long day) {

        return calendarService.getCustomerEvents(principal.getName(), day);
    }

    @ApiOperation(
            value = "Создание записи в календаре"
    )
    @PostMapping(value = "/note")
    public List<Event> doPostCalendarNote(
            Principal principal,
            @RequestParam(value = "hour", required = true) Integer[] hour,
            @RequestParam(value = "day", required = true) Long day,
            @RequestBody Text body) {

        return calendarService.createCalendarWithNote(principal.getName(), day, hour, body);
    }

    @ApiOperation(
            value = "Обновление записи в календаре"
    )
    @PutMapping(value = "/note")
    public List<Event> doPutCalendarNote(
            Principal principal,
            @RequestParam(value = "calendar_id", required = true) Long calendarId,
            @RequestBody Text body) {

        return calendarService.updateCalendarNote(principal.getName(), calendarId, body);
    }

    @ApiOperation(
            value = "Удаление записи из календаря"
    )
    @DeleteMapping(value = "/note")
    public List<Event> doDeleteCalendarNote(
            Principal principal,
            @RequestParam(value = "calendar_id", required = true) Long calendarId) {

        return calendarService.deleteCalendarNote(principal.getName(), calendarId);
    }
}

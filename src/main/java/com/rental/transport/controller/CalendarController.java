package com.rental.transport.controller;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Text;
import com.rental.transport.service.CalendarService;
import io.swagger.annotations.ApiOperation;
import java.security.Principal;
import java.util.List;
import lombok.AllArgsConstructor;
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

@RequestMapping(value = "/calendar")
@RestController
@AllArgsConstructor
public class CalendarController {

    private final CalendarService service;

    @ApiOperation(
        value = "Получение календаря заказчика транспорта"
    )
    @GetMapping(value = "/transport")
    public List<Event> doGetTransportCalendar(
        Principal principal,
        @RequestParam(value = "transport_id", required = true) Long transport_id,
        @RequestParam(value = "day", required = true) Long day) {

        return service.getTransportEvents(principal.getName(), day, transport_id);
    }

    @ApiOperation(
        value = "Получение календаря владельца транспорта"
    )
    @GetMapping(value = "/customer")
    public List<Event> doGetCustomerCalendar(
        Principal principal,
        @RequestParam(value = "day", required = true) Long day) {

        return service.getCustomerEvents(principal.getName(), day);
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

        return service.createCalendarWithNote(principal.getName(), day, hour, body);
    }

    @ApiOperation(
        value = "Обновление записи в календаре"
    )
    @PutMapping(value = "/note")
    public List<Event> doPutCalendarNote(
        Principal principal,
        @RequestParam(value = "calendar_id", required = true) Long calendarId,
        @RequestBody Text body) {

        return service.updateCalendarNote(principal.getName(), calendarId, body);
    }

    @ApiOperation(
        value = "Удаление записи из календаря"
    )
    @DeleteMapping(value = "/note")
    public List<Event> doDeleteCalendarNote(
        Principal principal,
        @RequestParam(value = "calendar_id", required = true) Long calendarId) {

        return service.deleteCalendarNote(principal.getName(), calendarId);
    }

    @ApiOperation(
        value = "Получение списка записей в календаре"
    )
    @GetMapping(value = "/list")
    public List<Event> doGetPagesCalendar(
        Principal principal,
        @RequestParam(value = "page", required = true) Integer page,
        @RequestParam(value = "size", required = true) Integer size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("id"));
        return service.getPage(pageable);
    }
}

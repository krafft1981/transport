package com.rental.transport.controller;

import com.rental.transport.model.Calendar;
import com.rental.transport.service.NetworkService;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value="/calendar")
@Controller
public class CalendarController {

    @GetMapping
    public String goGetCalendar(Model model) {
        Integer start = 1598904000;
        Integer stop = 1609358400;
        try {

            List<Calendar> calendarList = NetworkService
                    .getInstance()
                    .getCalendarApi()
                    .doGetCalendar(start, stop)
                    .execute()
                    .body()
                    .stream()
                    .map(calendar -> { return calendar; })
                    .collect(Collectors.toList());

            model.addAttribute("calendars", calendarList);
        }
        catch(IOException e) {

        }
        return "calendar";
    }
}

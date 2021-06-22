package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rental.transport.enums.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event extends AbstractDto {

    @JsonProperty("calendar")
    private Calendar calendar;
    @JsonProperty("order")
    private Order order = null;
    @JsonProperty("request")
    private Request request = null;
    @JsonProperty("type")
    private Integer type = 0;

    public Event(EventTypeEnum type, Long day, Integer[] hours) {

        setType(type.getId());
        setCalendar(new Calendar(day, hours));
    }

    public Event(EventTypeEnum type, Long id, Long day, Integer[] hours) {

        setType(type.getId());
        setCalendar(new Calendar(id, day, hours));
    }

    public Event(EventTypeEnum type, Long day, Integer start, Integer stop) {

        Integer index = 0;
        Integer[] diapazon = new Integer[stop - start];
        for (Integer hour = start; hour < stop; hour++, index++)
            diapazon[index] = hour;

        setType(type.getId());
        setCalendar(new Calendar(day, diapazon));
    }

    public Event(EventTypeEnum type, Long id, Long day, Integer start, Integer stop) {

        Integer index = 0;
        Integer[] diapazon = new Integer[stop - start];
        for (Integer hour = start; hour < stop; hour++, index++)
            diapazon[index] = hour;

        setType(type.getId());
        setCalendar(new Calendar(id, day, diapazon));
    }

    public Event(Calendar calendar) {

        setType(EventTypeEnum.BUSY.getId());
        setCalendar(calendar);
    }

    public Event(Order order) {

        setOrder(order);
        setType(EventTypeEnum.ORDER.getId());
        setCalendar(new Calendar(order.getDay(), order.getHours()));
    }

    public Event(EventTypeEnum type, Request request) {

        setRequest(request);
        setType(type.getId());
        setCalendar(new Calendar(request.getDay(), request.getHours()));
    }
}

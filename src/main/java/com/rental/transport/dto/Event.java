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
    @JsonProperty("note")
    private NoteBook note = null;
    @JsonProperty("type")
    private Integer type = 0;

    public Event(NoteBook note, Long day, Integer[] hours) {

        setType(EventTypeEnum.NOTE.getId());
        setNote(note);
        setCalendar(new Calendar(day, hours));
    }

    public Event(EventTypeEnum type, Long day, Integer[] hours) {

        setType(type.getId());
        setCalendar(new Calendar(day, hours));
    }

    public Event(EventTypeEnum type, Long day, Integer start, Integer stop) {

        Integer id = 0;
        Integer[] diapazon = new Integer[stop - start];
        for (Integer hour = start; hour < stop; hour++, id++)
            diapazon[id] = hour;

        setType(type.getId());
        setCalendar(new Calendar(day, diapazon));
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

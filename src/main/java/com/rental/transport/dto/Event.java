package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rental.transport.enums.EventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @JsonProperty("calendar")
    private Calendar calendar;
    @JsonProperty("order")
    private Order order;
    @JsonProperty("request")
    private Request request;
    @JsonProperty("type")
    private Long type;

    public Event(Calendar calendar, EventTypeEnum type) {

        setCalendar(calendar);
        setType(type.getId());
    }

    public Event(Order order, Calendar calendar) {

        setOrder(order);
        setCalendar(calendar);
        setType(EventTypeEnum.ORDER.getId());
    }

    public Event(Request request) {

        setRequest(request);
        setType(EventTypeEnum.REQUEST.getId());
    }
}

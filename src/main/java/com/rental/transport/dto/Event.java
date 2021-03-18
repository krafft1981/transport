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
    @JsonProperty("type")
    private Long type;

    public Event(Calendar calendar, Long type) {

        setCalendar(calendar);
        setType(type);
    }

    public Event(Order order) {

        setOrder(order);
        setType(EventTypeEnum.ORDER.getId());
    }
}

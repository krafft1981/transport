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

    @JsonProperty("order")
    private Order order = null;
    @JsonProperty("request")
    private Request request = null;
    @JsonProperty("type")
    private Integer type = 0;

    public Event(Integer type) {

        setType(type);
    }

    public Event(EventTypeEnum type) {

        setType(type.getId());
    }

    public Event(Order order) {

        setOrder(order);
        setType(EventTypeEnum.ORDER.getId());
    }

    public Event(Request request) {

        setRequest(request);
        setType(EventTypeEnum.REQUEST.getId());
    }
}

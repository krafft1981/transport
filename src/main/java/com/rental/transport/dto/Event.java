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
    @JsonProperty("object_id")
    private Long objectId = null;
    @JsonProperty("type")
    private String type = EventTypeEnum.UNCKNOWN.name();

    public Event(EventTypeEnum type, Long day, Integer start, Integer stop) {

        int index = 0;
        Integer[] hours = new Integer[stop - start];
        for (Integer hour = start; hour < stop; hour++, index++)
            hours[index] = hour;

        setType(type.name());
        setCalendar(new Calendar(day, hours, ""));
    }

    public Event(EventTypeEnum type, Calendar calendar) {

        setType(type.name());
        setCalendar(calendar);
    }

    public Event(EventTypeEnum type, Calendar calendar, Long objectId) {

        setType(type.name());
        setCalendar(calendar);
        setObjectId(objectId);
    }
}

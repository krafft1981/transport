package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rental.transport.enums.CalendarTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Calendar extends AbstractDto {

    @JsonProperty("day")
    private Long day;
    @JsonProperty("hours")
    private Integer[] hours;
    @JsonProperty("note")
    private String note = "";

    public Calendar(Long day, Integer[] hours) {

        setDay(day);
        setHours(hours);
    }

    public Calendar(Long day, Integer start, Integer stop) {

        setDay(day);
        Integer id = 0;
        hours = new Integer[stop - start];
        for (Integer hour = start; hour < stop; hour++, id++)
            hours[id] = hour;
    }

    public Integer minHour() {
        Integer min = Integer.MAX_VALUE;

        for (Integer value : hours) {
            if (min > value)
                min = value;
        }

        return min;
    }

    public Integer maxHour() {
        Integer max = Integer.MIN_VALUE;

        for (Integer value : hours) {
            if (max < value) max = value;
        }

        max++;
        return max;
    }
}

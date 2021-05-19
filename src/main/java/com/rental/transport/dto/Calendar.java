package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("message")
    private String message = "";

    public Calendar(Long day, Integer[] hours) {
        setDay(day);
        setHours(hours);
    }
}

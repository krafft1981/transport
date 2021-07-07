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
public class Calendar extends AbstractIdDto {

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
}

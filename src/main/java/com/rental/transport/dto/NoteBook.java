package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class NoteBook extends AbstractIdDto {

    @JsonProperty("text")
    private String text;
    @JsonProperty("customer_id")
    private Long customerId;
    @JsonProperty("calendar_id")
    private Long calendar;
    @JsonProperty("date")
    private Date date;
}

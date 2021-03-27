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
public class Message extends AbstractIdDto {

    @JsonProperty("text")
    private String text;
    @JsonProperty("customer_id")
    private Long customerId;
    @JsonProperty("date")
    private Long date;
}

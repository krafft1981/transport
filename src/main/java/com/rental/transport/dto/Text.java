package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Text {

    @JsonProperty("message")
    private String message;
}

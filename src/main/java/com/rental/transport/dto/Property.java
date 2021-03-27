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
public class Property extends AbstractIdDto {

    @JsonProperty("human_name")
    private String humanName;
    @JsonProperty("logic_name")
    private String logicName;
    @JsonProperty("value")
    private String value;
    @JsonProperty("type")
    private String type;
}

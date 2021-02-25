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
public class Templates extends AbstractDto {

    @JsonProperty("transport_type")
    private Type transportType;
    @JsonProperty("property_type")
    private PropertyType propertyType;
    @JsonProperty("value")
    private String value;
}

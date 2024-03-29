package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transport extends AbstractIdDto {

    @JsonProperty("type")
    private Type type;
    @JsonProperty("property")
    private Set<Property> property = new HashSet<>();
    @JsonProperty("parking")
    private Set<Long> parking = new HashSet<>();
    @JsonProperty("image")
    private Set<Long> image = new HashSet<>();
    @JsonProperty("customer")
    private Set<Long> customer = new HashSet<>();
}

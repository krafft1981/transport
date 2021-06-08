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
public class Order extends AbstractIdDto {

    @JsonProperty("property")
    private Set<Property> property = new HashSet<>();
    @JsonProperty("day")
    private Long day;
    @JsonProperty("hours")
    private Integer[] hours;
    @JsonProperty("message")
    private Set<Message> message = new HashSet<>();
}

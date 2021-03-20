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
public class Order extends AbstractDto {

    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("confirmed_at")
    private Long confirmedAt;
    @JsonProperty("status")
    private String status;
    @JsonProperty("property")
    private Set<Property> property = new HashSet<>();
}

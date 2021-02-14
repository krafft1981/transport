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
public class Parking extends AbstractDto {

    @JsonProperty("image")
    private Set<Long> image = new HashSet<>();
    @JsonProperty("customer")
    private Set<Long> customer = new HashSet<>();
    @JsonProperty("transport")
    private Set<Long> transport = new HashSet<>();
    @JsonProperty("property")
    private Set<Property> property = new HashSet<>();

    public void addImage(Long id) {
        image.add(id);
    }

    public void addCustomer(Long id) {
        customer.add(id);
    }

    public void addTransport(Long id) {
        transport.add(id);
    }
}

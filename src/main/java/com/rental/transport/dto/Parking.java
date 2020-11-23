package com.rental.transport.dto;

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

    private Set<Long> image = new HashSet<>();
    private Set<Long> customer = new HashSet<>();
    private Set<Long> transport = new HashSet<>();
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

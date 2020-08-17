package com.rental.transport.model;

import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Parking {

    private Long id;
    private String address;
    private String description;
    private Set<Long> customer = new HashSet<>();
    private Set<Long> transport = new HashSet<>();

    public void addCustomer(Long id) {
        customer.add(id);
    }

    public void addTransport(Long id) {
        transport.add(id);
    }
}

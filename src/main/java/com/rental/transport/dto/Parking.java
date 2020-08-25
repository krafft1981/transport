package com.rental.transport.dto;

import java.util.HashSet;
import java.util.Set;

public class Parking extends AbstractDto {

    private String address;
    private String description;
    private Double latitude;
    private Double Longitude;
    private Set<Long> customer = new HashSet<>();
    private Set<Long> transport = new HashSet<>();

    public void addCustomer(Long id) {
        customer.add(id);
    }

    public void addTransport(Long id) {
        transport.add(id);
    }
}

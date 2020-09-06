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

    private String name;
    private String address;
    private String description;
    private Double latitude;
    private Double longitude;
    private Set<Long> images = new HashSet<>();
    private Set<Long> customers = new HashSet<>();
    private Set<Long> transports = new HashSet<>();

    public void addImage(Long id) {
        images.add(id);
    }

    public void addCustomer(Long id) {
        customers.add(id);
    }

    public void addTransport(Long id) {
        transports.add(id);
    }
}

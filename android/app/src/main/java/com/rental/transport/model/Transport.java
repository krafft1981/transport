package com.rental.transport.model;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;
import lombok.Data;

@Data
public class Transport {

    private Long id;
    private String name;
    private String type;
    private Blob image;
    private Integer capacity;
    private String description;
    private Set<Long> drivers = new HashSet<>();

    public void addDrivers(Long id) {
        drivers.add(id);
    }
}

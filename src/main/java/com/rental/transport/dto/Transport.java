package com.rental.transport.dto;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;

public class Transport  extends AbstractDto {

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

package com.rental.transport.dto;

import java.sql.Blob;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transport {

    private Long id;
    private String name;
    private String type;
    private Set<Blob> image = new HashSet<>();
    private Integer capacity;
    private String description;
    private Set<Long> drivers = new HashSet<>();

    public void addDrivers(Long id) {
        drivers.add(id);
    }
}

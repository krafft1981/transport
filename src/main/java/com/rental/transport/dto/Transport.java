package com.rental.transport.dto;

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
    private String number;
    private Integer capacity;
    private String description;
    private Set<Long> drivers = new HashSet<>();
}

package com.rental.transport.dto;

import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Driver {

    private Long id;
    private String sirname;
    private String name;
    private String lastName;
    private String phone;
    private String image;
    private Set<Long> transport = new HashSet<>();
}

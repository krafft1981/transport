package com.rental.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    private Long id;
    private String sirname;
    private String name;
    private String lastName;
    private String phone;
    private String image;
}

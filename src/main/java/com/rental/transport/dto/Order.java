package com.rental.transport.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractDto {

    private Long customer;
    private Transport transport;
    private List<Customer> driver = new ArrayList<>();
    private List<Calendar> calendar = new ArrayList<>();
    private Set<Property> property = new HashSet<>();
    private Integer createdAt;
}

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
public class Transport extends AbstractDto {

    private Type type;
    private Set<Property> property = new HashSet<>();
    private Set<Long> parking = new HashSet<>();
    private Set<Long> image = new HashSet<>();
    private Set<Long> customer = new HashSet<>();

    public void addCustomer(Long id) {
        customer.add(id);
    }
    public void addImage(Long id) {
        image.add(id);
    }
    public void addParking(Long id) {
        parking.add(id);
    }
}

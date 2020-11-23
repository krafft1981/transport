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
public class Customer extends AbstractDto {

    private String account;
    private Set<Property> property = new HashSet<>();
    private Set<Long> image = new HashSet<>();
    private Set<Long> transport = new HashSet<>();
    private Set<Long> parking = new HashSet<>();

    public void addProperty(Property value) {
        property.add(value);
    }

    public void addImage(Long value) {
        image.add(value);
    }

    public void addTransport(Long value) {
        transport.add(value);
    }

    public void addParking(Long value) {
        parking.add(value);
    }
}

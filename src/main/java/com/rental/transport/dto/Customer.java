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
    private String firstName;
    private String lastName;
    private String family;
    private String phone;
    private Set<Long> images = new HashSet<>();
    private Set<Long> transports = new HashSet<>();
    private Set<Long> parkings = new HashSet<>();

    public void addImage(Long id) {
        images.add(id);
    }

    public void addTransport(Long id) {
        transports.add(id);
    }

    public void addParking(Long id) {
        parkings.add(id);
    }
}

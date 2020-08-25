package com.rental.transport.dto;

import java.sql.Blob;
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
    private Blob image;
    private Set<Long> transport = new HashSet<>();

    public void addTransport(Long id) {
        transport.add(id);
    }
}

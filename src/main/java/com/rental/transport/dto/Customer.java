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

    private String account = "";
    private String firstName = "";
    private String lastName = "";
    private String family = "";
    private String phone = "";
    private Integer startWorkAt;
    private Integer stopWorkAt;
    private Boolean workAtWeekEnd;
    private Set<Long> image = new HashSet<>();
    private Set<Long> transport = new HashSet<>();
    private Set<Long> parking = new HashSet<>();

    public void addImage(Long id) {
        image.add(id);
    }

    public void addTransport(Long id) {
        transport.add(id);
    }

    public void addParking(Long id) {
        parking.add(id);
    }
}

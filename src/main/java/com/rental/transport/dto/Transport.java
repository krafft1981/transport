package com.rental.transport.dto;

import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Transport  extends AbstractDto {

    private String name;
    private String type;
    private Integer capacity;
    private String description;
    private Currency cost;
    private Long parking;
    private List<Long> images = new ArrayList<>();
    private List<Long> customers = new ArrayList<>();
    public void addCustomer(Long id) {
        customers.add(id);
    }
    public void addImage(Long id) {
        images.add(id);
    }
}

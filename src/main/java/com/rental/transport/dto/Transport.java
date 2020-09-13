package com.rental.transport.dto;

import java.util.ArrayList;
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

    private String name = "";
    private String type = "";
    private Integer capacity = 1;
    private String description = "";
    private Double latitude = 0.0;
    private Double longitude = 0.0;
    private Double cost = 0.0;
    private List<Long> parking = new ArrayList<>();
    private List<Long> image = new ArrayList<>();
    private List<Long> customer = new ArrayList<>();
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

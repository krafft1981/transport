package com.rental.transport.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractDto {

    private String customerName;
    private String customerPhone;
    private Long customer;

    private Transport transport;

    private List<Customer> driver;

    private Double latitude;
    private Double longitude;

    private List<Calendar> calendar;

    private Integer createdAt;

    private Double cost;
    private Double price;

    private String comment;
    private String state;
}

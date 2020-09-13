package com.rental.transport.dto;

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

    private Long transport;

    private String driverName;
    private String driverPhone;
    private Long driver;

    private Double latitude;
    private Double longitude;

    private Integer startAt;
    private Integer stopAt;
    private Integer createdAt;

    private Double cost;
    private Double price;

    private String comment;
    private String state;
}

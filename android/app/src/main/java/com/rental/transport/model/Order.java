package com.rental.transport.model;

import java.util.Date;
import lombok.Data;

@Data
public class Order {

    private Long id;
    private Long driverId;
    private Long transportId;
    private Long customerId;
    private Date startAt;
    private Date stopAt;
    private String description;
    private String comment;
}

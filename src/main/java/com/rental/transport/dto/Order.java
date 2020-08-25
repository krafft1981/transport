package com.rental.transport.dto;

import java.util.Date;

public class Order extends AbstractDto {

    private Long driverId;
    private Long transportId;
    private Long customerId;
    private Date startAt;
    private Date stopAt;
    private String description;
    private String comment;
}

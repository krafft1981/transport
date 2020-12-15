package com.rental.transport.dto;

import java.util.Date;
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

    private String status;

    private Customer customer;
    private Transport transport;

    private List<Property> property;
    private List<Customer> driver;
    private List<Calendar> calendar;
    private List<Message> message;

    private Date createdAt;
}

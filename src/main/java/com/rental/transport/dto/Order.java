package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("status")
    private String status;
    @JsonProperty("customer")
    private Customer customer;
    @JsonProperty("transport")
    private Transport transport;
    @JsonProperty("property")
    private List<Property> property;
    @JsonProperty("driver")
    private List<Customer> driver;
    @JsonProperty("calendar")
    private List<Calendar> calendar;
    @JsonProperty("message")
    private List<Message> message;
    @JsonProperty("created_at")
    private Long createdAt;
}

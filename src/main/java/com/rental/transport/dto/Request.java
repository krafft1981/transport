package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request extends AbstractIdDto {

    @JsonProperty("created_at")
    private Long createdAt;
    @JsonProperty("interact_at")
    private Long interactAt;
    @JsonProperty("order")
    private Order order;
    @JsonProperty("customer")
    private Customer customer;
    @JsonProperty("driver")
    private Customer driver;
    @JsonProperty("transport")
    private Transport transport;
    @JsonProperty("day")
    private Long day;
    @JsonProperty("hours")
    private Integer[] hours;

    public Request(Customer customer, Customer driver, Transport transport, Long day, Integer[] hours) {

        setCustomer(customer);
        setDriver(driver);
        setTransport(transport);
        setDay(day);
        setHours(hours);
    }
}

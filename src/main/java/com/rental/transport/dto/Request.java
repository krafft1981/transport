package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.TransportEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Request extends AbstractDto {

    @JsonProperty("customer")
    private CustomerEntity customer;
    @JsonProperty("driver")
    private CustomerEntity driver;
    @JsonProperty("transport")
    private TransportEntity transport;
}

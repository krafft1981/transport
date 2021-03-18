package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.TransportEntity;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order extends AbstractDto {

    @JsonProperty("created_at")
    private Long created;
    @JsonProperty("status")
    private String status;
    @JsonProperty("property")
    private Set<Property> property = new HashSet<>();
    @JsonProperty("calendar")
    private Set<Calendar> calendar = new HashSet<>();
    @JsonProperty("driver")
    private Set<Customer> driver = new HashSet<>();

//    @JsonProperty("customer")
//    private Customer customer;
//    @JsonProperty("transport")
//    private Transport transport;
}

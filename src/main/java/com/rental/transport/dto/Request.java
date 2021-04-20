package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.RequestStatusEnum;
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
    private Long order;
    @JsonProperty("customer")
    private CustomerEntity customer;
    @JsonProperty("driver")
    private CustomerEntity driver;
    @JsonProperty("transport")
    private TransportEntity transport;
    @JsonProperty("status")
    private RequestStatusEnum status;
}

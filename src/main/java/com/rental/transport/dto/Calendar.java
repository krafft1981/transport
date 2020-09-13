package com.rental.transport.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Calendar extends AbstractDto {

    private Long customerId;
    private Integer startAt;
    private Integer stopAt;
    private Long orderId;

    public Calendar(Long customerId) {
        setCustomerId(customerId);
    }
}

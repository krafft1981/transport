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

    private Long startAt;
    private Long stopAt;
    private Long dayNum;
    private Long order = null;
    private Long customerId;

    public Calendar(Long startAt, Long stopAt, Long dayNum, Customer customer) {

        setStartAt(startAt);
        setStopAt(stopAt);
        setDayNum(dayNum);
        setCustomerId(customer.getId());
    }
}

package com.rental.transport.dto;

import java.util.HashSet;
import java.util.Set;
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
    private Set<Order> order = new HashSet<>();
    private Set<Customer> customer = new HashSet<>();

    public Calendar(Long startAt, Long stopAt, Long dayNum) {

        setStartAt(startAt);
        setStopAt(stopAt);
        setDayNum(dayNum);
    }
}

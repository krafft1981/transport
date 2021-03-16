package com.rental.transport.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rental.transport.enums.CalendarEventTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Calendar extends AbstractDto {

    @JsonProperty("start_at")
    private Long startAt;
    @JsonProperty("stop_at")
    private Long stopAt;
    @JsonProperty("day_num")
    private Long dayNum;
    @JsonProperty("order")
    private Long order = null;
    @JsonProperty("customer")
    private Long customer;
    @JsonProperty("type")
    private Long type;

    public Calendar(Long startAt, Long stopAt, Long dayNum, Customer customer, CalendarEventTypeEnum type) {

        setStartAt(startAt);
        setStopAt(stopAt);
        setDayNum(dayNum);
        setCustomer(customer.getId());
        setType(type.getId());
    }
}

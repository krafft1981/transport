package com.rental.transport.dto;

import com.rental.transport.dto.template.AbstractEnabledDto;
import com.rental.transport.entity.CalendarEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class MessageDto extends AbstractEnabledDto {

    private String text;
    private CustomerDto customer;
    private CalendarEntity calendar;
}

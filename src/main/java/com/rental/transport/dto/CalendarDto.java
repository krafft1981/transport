package com.rental.transport.dto;

import com.rental.transport.dto.template.AbstractEnabledDto;
import com.rental.transport.enums.CalendarTypeEnum;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.UUID;

@Setter
@Getter
@Accessors(chain = true)
public class CalendarDto extends AbstractEnabledDto {

    private Long day;
    private Integer[] hours;
    private CalendarTypeEnum type;
    private UUID objectId;
    private UUID orderId;
    private MessageDto message;
}

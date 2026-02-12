package com.rental.transport.dto;

import com.rental.transport.dto.template.AbstractCreatableDto;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.MessageEntity;
import com.rental.transport.entity.template.AbstractCreatableEntity;
import com.rental.transport.enums.RequestStatusEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto extends AbstractCreatableDto {

    private CalendarDto calendar;
    private RequestStatusEnum status;
    private UUID customer;
    private UUID driver;
    private UUID transport;
    private Set<UUID> properties;
    private OrderDto order;
    private Set<MessageDto> message;
}

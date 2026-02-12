package com.rental.transport.mapper;

import com.rental.transport.dto.CalendarDto;
import com.rental.transport.entity.CalendarEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CalendarMapper {

    public abstract CalendarEntity toEntity(CalendarDto dto);
    public abstract CalendarDto toDto(CalendarEntity entity);
}

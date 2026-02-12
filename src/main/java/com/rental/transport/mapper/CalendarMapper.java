package com.rental.transport.mapper;

import com.rental.transport.dto.CalendarDto;
import com.rental.transport.entity.CalendarEntity;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        uses = {
                MessageMapper.class,
                CustomerMapper.class
        })
public abstract class CalendarMapper {

    public abstract CalendarEntity dtoToEntity(CalendarDto dto);
    public abstract CalendarDto entityToDto(CalendarEntity entity);
}

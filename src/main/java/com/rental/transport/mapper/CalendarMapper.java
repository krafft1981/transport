package com.rental.transport.mapper;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CalendarEntity;
import java.util.Objects;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CalendarMapper implements AbstractMapper<CalendarEntity, Calendar> {

    @Autowired
    private ModelMapper mapper;

    @Override
    public CalendarEntity toEntity(Calendar dto) {
        return Objects.isNull(dto) ? null : mapper.map(dto, CalendarEntity.class);
    }

    @Override
    public Calendar toDto(CalendarEntity entity) {
        return Objects.isNull(entity) ? null : mapper.map(entity, Calendar.class);
    }
}

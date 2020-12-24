package com.rental.transport.mapper;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CalendarEntity;
import java.util.Objects;
import javax.annotation.PostConstruct;
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

    @PostConstruct
    public void postConstruct() {
        mapper.createTypeMap(CalendarEntity.class, Calendar.class)
                .addMappings(m -> m.skip(Calendar::setCustomer))
                .setPostConverter(toDtoConverter());

        mapper.createTypeMap(Calendar.class, CalendarEntity.class)
                .setPostConverter(toEntityConverter());
    }

    public void mapSpecificFields(CalendarEntity source, Calendar destination) {

        destination.setId(Objects.isNull(source) || Objects.isNull(source.getId()) ? null : source.getId());
        destination.setCustomer(source.getCustomer().getId());
    }
}

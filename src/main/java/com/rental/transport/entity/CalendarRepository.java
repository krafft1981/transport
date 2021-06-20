package com.rental.transport.entity;

import com.rental.transport.enums.CalendarTypeEnum;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends IRepository<CalendarEntity> {

    List<CalendarEntity> findByDayAndTypeAndObjectId(Long day, CalendarTypeEnum type, Long objectId);
}

package com.rental.transport.entity;

import com.rental.transport.enums.CalendarTypeEnum;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CalendarRepository extends IRepository<CalendarEntity> {

    List<CalendarEntity> findByDayAndTypeAndObjectId(Long day, CalendarTypeEnum type, Long objectId);
    List<CalendarEntity> findByDayAndObjectId(Long day, Long objectId);
    void deleteByDayAndTypeAndObjectId(Long day, CalendarTypeEnum request, Long objectId);
}

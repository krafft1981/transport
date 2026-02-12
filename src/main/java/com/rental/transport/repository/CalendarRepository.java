package com.rental.transport.repository;

import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.enums.CalendarTypeEnum;
import com.rental.transport.repository.template.IRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CalendarRepository extends IRepository<CalendarEntity> {

    List<CalendarEntity> findByDayAndTypeAndObjectId(Long day, CalendarTypeEnum type, UUID objectId);
    List<CalendarEntity> findByDayAndObjectId(Long day, UUID objectId);
    void deleteByDayAndTypeAndObjectId(Long day, CalendarTypeEnum request, UUID objectId);
}

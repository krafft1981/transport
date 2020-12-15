package com.rental.transport.entity;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends AbstractRepository<CalendarEntity> {

    @Query("select c from calendar c where day_num = :day and (" +
            "(:start <= start_at and :stop >= stop_at) or " +
            "(:start > start_at and :stop < stop_at) or " +
            "(:start >= start_at and :start < stop_at) or " +
            "(:stop >= start_at and :stop < stop_at))")
    List<CalendarEntity> findByDayAndStartAndStop(
            @Param("day") Long day,
            @Param("start") Date start,
            @Param("stop") Date stop
    );
}

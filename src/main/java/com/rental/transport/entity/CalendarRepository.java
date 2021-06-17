package com.rental.transport.entity;

import com.rental.transport.enums.CalendarTypeEnum;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends IRepository<CalendarEntity> {

    @Query(
            nativeQuery = true,
            value = "select c.id, c.day, c.hours, c.type, c.object_id from calendar " +
                    "where day = :day"
    )
    List<CalendarEntity> findCalendarByDay(
            @Param("day") Long day
    );

    @Query(
            nativeQuery = true,
            value = "select c.id, c.day, c.hours, c.type, c.object_id from calendar " +
                    "where day = :day and type = :type"
    )
    List<CalendarEntity> findCalendarByDayAndType(
            @Param("day") Long day,
            @Param("type") CalendarTypeEnum type
    );

    @Query(
            nativeQuery = true,
            value = "select c.id, c.day, c.hours, c.type, c.object_id from calendar " +
                    "where day = :day and type = :type and object_id = :objectId"
    )
    List<CalendarEntity> findCalendarByDayAndTypeAndObjectId(
            @Param("day") Long day,
            @Param("type") CalendarTypeEnum type,
            @Param("objectId") Long objectId
    );
}

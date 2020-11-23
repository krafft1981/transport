package com.rental.transport.entity;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface CalendarRepository extends AbstractRepository<CalendarEntity> {

    @Query("select c from calendar c where day_num = :day and (" +
            "(:start <= start_at and :stop >= stop_at) or " +
            "(:start > start_at and :stop < stop_at) or " +
            "(:start >= start_at and :start < stop_at) or " +
            "(:stop >= start_at and :stop < stop_at))")
    List<CalendarEntity> findByDayNumAndStartAndStop(
            @Param("day") Long day,
            @Param("start") Date start,
            @Param("stop") Date stop
    );

    @Transactional
    @Modifying
    @Query("delete from calendar where customer_id = :customer and " +
            "day_num = :day and (" +
            "(:start <= start_at and :stop >= stop_at) or " +
            "(:start > start_at and :stop < stop_at) or " +
            "(:start >= start_at and :start < stop_at) or " +
            "(:stop >= start_at and :stop < stop_at))")
    void deleteByCustomerIdAndDayAndStartAndStop(
            @Param("customer") Long customer,
            @Param("day") Long day,
            @Param("start") Date start,
            @Param("stop") Date stop
    );

    @Query("select c from calendar c where customer_id = :customer and " +
            "day_num = :day and (" +
            "(:start <= start_at and :stop >= stop_at) or " +
            "(:start > start_at and :stop < stop_at) or " +
            "(:start >= start_at and :start < stop_at) or " +
            "(:stop >= start_at and :stop < stop_at))")
    List<CalendarEntity> findByCustomerIdAndDayAndStartAndStop(
            @Param("customer") Long customer,
            @Param("day") Long day,
            @Param("start") Date start,
            @Param("stop") Date stop
    );
}

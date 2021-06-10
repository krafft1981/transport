package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends IRepository<CalendarEntity> {

    // Получаем календарь пользователя
    @Query(
            nativeQuery = true,
            value = "select c.id, c.day, c.hours from calendar c join customer_calendar l on c.id = l.calendar_id where " +
                    "c.day = :day and l.customer_id = :customerId"
    )
    List<CalendarEntity> findCalendarByCustomerIdAndDay(
            @Param("customerId") Long customerId,
            @Param("day") Long day
    );

    // Получаем календарь транспорта
    @Query(
            nativeQuery = true,
            value = "select c.id, c.day, c.hours from calendar c join transport_calendar l on c.id = l.calendar_id " +
                    "where c.day = :day and l.transport_id = :transportId"
    )
    List<CalendarEntity> findCalendarByTransportIdAndDay(
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    // Получаем конкретную запись с календаря
    CalendarEntity findByDayAndHours(Long day, Integer[] hours);
}

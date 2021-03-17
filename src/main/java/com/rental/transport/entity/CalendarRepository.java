package com.rental.transport.entity;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends IRepository<CalendarEntity> {

    @Query(
            nativeQuery = true,
            value = "select c.id, c.day_num, c.start_at, c.stop_at from calendar c join customer_calendar l on c.id = l.calendar_id where c.day_num = :day and l.customer_id = :customerId"
    )
    List<CalendarEntity> findCustomerCalendarByDay(
            @Param("customerId") Long customerId,
            @Param("day") Long day
    );

    @Query(
            nativeQuery = true,
            value = "select c.id, c.day_num, c.start_at, c.stop_at from calendar c join transport_calendar l on c.id = l.calendar_id where c.day_num = :day and l.transport_id = :transportId"
    )
    List<CalendarEntity> findTransportCalendarByDay(
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    @Query(
            nativeQuery = true,
            value = "select c.id, c.day_num, c.start_at, c.stop_at from calendar c join orders_calendar l on c.id = l.calendar_id where c.day_num = :day and l.order_id = :orderId"
    )
    List<CalendarEntity> findOrderCalendarByDay(
            @Param("orderId") Long orderId,
            @Param("day") Long day
    );

    CalendarEntity findByDayNumAndStartAtAndStopAt(Long day, Date start, Date stop);
}

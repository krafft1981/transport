package com.rental.transport.entity;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends AbstractRepository<CalendarEntity> {

    @Query("select c from calendar c where customer_id = :id and ((:start >= start_at and :start < stop_at) or (:stop >= start_at and :stop < stop_at))")
    List<CalendarEntity> findByCustomerIdUseStartAndStop(
            @Param("id") Long id,
            @Param("start") Date start,
            @Param("stop") Date stop
    );
}


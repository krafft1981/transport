package com.rental.transport.entity;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CalendarRepository extends JpaRepository<CalendarEntity, Long> {

    @Query("select c from calendar c where customer_id = :id and (start_at >= :start and stop_at <= :stop)")
    List<CalendarEntity> getEntityByCustomerIdUseStartAndStop(
            @Param("id") Long id,
            @Param("start") Date start,
            @Param("stop") Date stop
    );
}

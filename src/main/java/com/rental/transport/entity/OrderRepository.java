package com.rental.transport.entity;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends AbstractRepository<OrderEntity> {

    @Query("select o from orders o where state = 'Confirmed' and CURRENT_TIMESTAMP > stop_at")
    List<OrderEntity> findByStateConfirmed();

    @Query("select count(o) from orders o where transport_id = :id and ((:start >= start_at and :start < stop_at) or (:stop >= start_at and :stop < stop_at))")
    Long countByTransportWhereStartStopBusy(Long id, Date start, Date stop);
}

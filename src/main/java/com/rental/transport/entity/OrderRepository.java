package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends AbstractRepository<OrderEntity> {

/*
    @Query("select o from orders o where state = 'Confirmed' and CURRENT_TIMESTAMP > stop_at")
    List<OrderEntity> findByStateConfirmed();

    @Query("select count(o) from orders o where transport_id = :id and state in ('New', 'Confirmed') and (" +
    Long countByTransportWhereStartStopBusy(Long id, Date start, Date stop);

    @Query("select o from orders o where transport_id = :id and state in ('New', 'Confirmed') and (" +
    List<OrderEntity> findByTransportUseStartAndStop(Long id, Date start, Date stop);

    @Query("select o from orders o where customer_id = :id and state in ('New', 'Confirmed') order by id")
    List<OrderEntity> findByCustomerIdAndState(Long id);
*/
    @Transactional
    @Modifying
    @Query("update orders set state = :state where id = :id")
    void updateOrderState(
            @Param("id") Long id,
            @Param("state") String state
    );
}

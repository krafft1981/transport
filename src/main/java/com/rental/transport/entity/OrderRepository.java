package com.rental.transport.entity;

import com.rental.transport.enums.OrderStatusEnum;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRepository extends IRepository<OrderEntity> {

    List<OrderEntity> findByCustomer(CustomerEntity entity, Pageable pageable);

    List<OrderEntity> findByTransport(TransportEntity entity, Pageable pageable);

    @Transactional
    @Modifying
    @Query("update orders set state = :state where id = :id")
    void updateOrderState(
            @Param("id") Long id,
            @Param("state") OrderStatusEnum state
    );
}

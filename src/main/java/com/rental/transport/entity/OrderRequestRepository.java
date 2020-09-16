package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderRequestRepository extends AbstractRepository<OrderRequestEntity> {

    Long countByOrderId(Long id);

    @Transactional
    @Modifying
    void deleteByOrderId(Long id);

    @Transactional
    @Modifying
    @Query("delete from order_request where order_id = :orderId and customer_id = :customerId")
    void deleteByOrderIdAndCustomerId(Long orderId, Long customerId);

    List<OrderRequestEntity> findByCustomerId(Long id);
}

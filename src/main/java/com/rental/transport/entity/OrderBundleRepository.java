package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface OrderBundleRepository extends AbstractRepository<OrderBundleEntity> {

    Long countByOrderId(Long id);

    @Transactional
    @Modifying
    void deleteByOrderId(Long id);

    @Transactional
    @Modifying
    @Query("delete from orders_bundle where order_id = :order_id and customer_id = :customer_id")
    void deleteByOrderIdAndCustomerId(
            @Param("order_id") Long orderId,
            @Param("customer_id") Long customerId
    );

    List<OrderBundleEntity> findByCustomerId(Long id);
}

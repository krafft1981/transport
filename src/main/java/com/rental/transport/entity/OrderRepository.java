package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends IRepository<OrderEntity> {

    List<OrderEntity> findByCustomer(CustomerEntity entity, Pageable pageable);
    List<OrderEntity> findByTransport(TransportEntity entity, Pageable pageable);
}

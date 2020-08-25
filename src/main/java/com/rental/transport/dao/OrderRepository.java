package com.rental.transport.dao;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends AbstractRepository<OrderEntity> {

    OrderEntity findByCustomerId(@NonNull Long id);
    OrderEntity findByDriverId(@NonNull Long id);
    OrderEntity findByTransportId(@NonNull Long id);
}

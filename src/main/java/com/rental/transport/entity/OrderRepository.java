package com.rental.transport.entity;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends AbstractRepository<OrderEntity> {

//    OrderEntity findByCustomerId(@NonNull Long id);
//    OrderEntity findByDriverId(@NonNull Long id);
//   OrderEntity findByTransportId(@NonNull Long id);
}

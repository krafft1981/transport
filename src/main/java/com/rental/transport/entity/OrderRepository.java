package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends IRepository<OrderEntity> {

    List<OrderEntity> findByCustomerOrderByIdDesc(CustomerEntity customer, Pageable pageable);
    List<OrderEntity> findByCustomerAndDay(CustomerEntity customer, Long day);
    List<OrderEntity> findByTransportOrderByIdDesc(TransportEntity transport, Pageable pageable);
}

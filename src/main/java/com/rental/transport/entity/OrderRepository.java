package com.rental.transport.entity;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends IRepository<OrderEntity> {

    List<OrderEntity> findByCustomer(CustomerEntity customer);
    List<OrderEntity> findByDriver(CustomerEntity customer);
    List<OrderEntity> findByTransport(TransportEntity transport);
}

package com.rental.transport.entity;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends IRepository<ReviewEntity> {

    void deleteByCustomerAndTransport(CustomerEntity customer, TransportEntity transport);
    List<ReviewEntity> findByTransport(TransportEntity transport);
    ReviewEntity findByCustomerAndTransport(CustomerEntity customer, TransportEntity transport);
}


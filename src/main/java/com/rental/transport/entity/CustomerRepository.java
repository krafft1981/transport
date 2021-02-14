package com.rental.transport.entity;

import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends IRepository<CustomerEntity> {

    CustomerEntity findByAccount(String account);
}


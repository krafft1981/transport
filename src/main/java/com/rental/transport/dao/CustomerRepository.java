package com.rental.transport.dao;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends AbstractRepository<CustomerEntity> {

    CustomerEntity findByAccount(@NonNull String account);
}

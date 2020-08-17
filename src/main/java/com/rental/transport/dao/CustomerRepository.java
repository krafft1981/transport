package com.rental.transport.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends PagingAndSortingRepository<CustomerEntity, Long> {

    CustomerEntity findByAccount(@NonNull String account);
}

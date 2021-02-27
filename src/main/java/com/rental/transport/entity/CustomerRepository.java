package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends IRepository<CustomerEntity> {

    CustomerEntity findByEnableTrueAndAccount(String account);
    List<CustomerEntity> findAllByEnableTrue(Pageable pageable);
}


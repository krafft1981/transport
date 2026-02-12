package com.rental.transport.repository;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.repository.template.IRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends IRepository<CustomerEntity> {

}

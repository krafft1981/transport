package com.rental.transport.repository;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.repository.template.IRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends IRepository<CustomerEntity> {

    List<CustomerEntity> findAllByEnableTrue();
}

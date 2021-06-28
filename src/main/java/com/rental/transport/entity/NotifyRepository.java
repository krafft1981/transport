package com.rental.transport.entity;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface NotifyRepository extends IRepository<NotifyEntity> {

    List<NotifyEntity> findByCustomerOrderById(CustomerEntity customer);
}


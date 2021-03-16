package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends IRepository<ConfirmationEntity> {

    List<ConfirmationEntity> getByCustomer(Long id, Pageable pageable);

    List<ConfirmationEntity> getByOrder(Long id, Pageable pageable);

    void deleteByOrderId(Long id);

    void deleteByCustomerIdAndOrderId(CustomerEntity customer, OrderEntity order);
}

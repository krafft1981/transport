package com.rental.transport.entity;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends AbstractRepository<ConfirmationEntity> {

    List<ConfirmationEntity> getByCustomer(Long id);
    List<ConfirmationEntity> getByOrder(Long id);
    void deleteByOrderId(Long id);
    void deleteByCustomerIdAndOrderId(CustomerEntity customer, OrderEntity order);
}

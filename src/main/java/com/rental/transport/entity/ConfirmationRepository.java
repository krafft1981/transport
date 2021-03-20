package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfirmationRepository extends IRepository<ConfirmationEntity> {

    List<ConfirmationEntity> getByCustomerId(Long customerId);
    List<ConfirmationEntity> getByCustomerId(Long customerId, Pageable pageable);
    List<ConfirmationEntity> getByOrderId(Long orderId, Pageable pageable);

    void deleteByOrderId(Long id);

    void deleteByCustomerIdAndOrderId(Long customer, Long order);
}

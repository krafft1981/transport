package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends IRepository<TransportEntity> {

    List<TransportEntity> findAllByEnableTrueAndTypeId(Pageable pageable, Long type);
    List<TransportEntity> findAllByCustomerIdAndEnableTrue(Long id);
}

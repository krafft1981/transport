package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportTypeRepository extends IRepository<TransportTypeEntity> {

    TransportTypeEntity findByEnableTrueAndName(String name);
    List<TransportTypeEntity> findAllByEnableTrue(Pageable pageable);
}

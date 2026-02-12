package com.rental.transport.repository;

import com.rental.transport.entity.TransportEntity;
import com.rental.transport.repository.template.IRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransportRepository extends IRepository<TransportEntity> {

    List<TransportEntity> findAllByEnableTrue(Pageable pageable);
    List<TransportEntity> findAllByEnableTrueAndTypeId(Pageable pageable, Long type);
//    List<TransportEntity> findAllByCustomerIdAndEnableTrue(UUID id);
}

package com.rental.transport.repository;

import com.rental.transport.entity.TransportTypeEntity;
import com.rental.transport.repository.template.IRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransportTypeRepository extends IRepository<TransportTypeEntity> {

    TransportTypeEntity findByEnableTrueAndName(String name);
    List<TransportTypeEntity> findAllByEnableTrue(Pageable pageable);
}

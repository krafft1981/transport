package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends AbstractRepository<TransportEntity> {

    List<TransportEntity> findAllByTypeId(Pageable pageable, Long type);
    List<TransportEntity> findAllByCustomerId(Long id);
}

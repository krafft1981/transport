package com.rental.transport.entity;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends AbstractRepository<TransportEntity> {

    TransportEntity findByType(@NonNull String type);
}

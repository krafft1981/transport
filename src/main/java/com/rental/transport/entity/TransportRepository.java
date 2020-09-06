package com.rental.transport.entity;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends AbstractRepository<TransportEntity> {

    Long findCountByType(@NonNull String type);
//    List<TransportEntity> findAllByType(@NonNull String type, Pageable pageable);
//    List<TransportEntity> findAllByTypeAndCustomerAccount(@NonNull String account, @NonNull String type, Pageable pageable);
}

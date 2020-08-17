package com.rental.transport.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportRepository extends PagingAndSortingRepository<TransportEntity, Long> {

//    List<TransportEntity> findByAccount(@NonNull String account);
}

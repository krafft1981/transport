package com.rental.transport.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends PagingAndSortingRepository<ParkingEntity, Long> {
}

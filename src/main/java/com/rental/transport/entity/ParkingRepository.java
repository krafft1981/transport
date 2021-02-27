package com.rental.transport.entity;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends IRepository<ParkingEntity> {

    List<ParkingEntity> findAllByCustomerId(Long id);
}

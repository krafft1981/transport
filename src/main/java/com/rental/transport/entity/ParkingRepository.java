package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends IRepository<ParkingEntity> {

    List<ParkingEntity> findAllByCustomerId(Long id);
    List<ParkingEntity> findAllByEnableTrue(Pageable pageable);
}

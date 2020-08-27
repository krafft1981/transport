package com.rental.transport.entity;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends AbstractRepository<ParkingEntity> {

    ParkingEntity findByAddress(@NonNull String address);
    ParkingEntity findByCustomer(@NonNull String customer);
}

package com.rental.transport.entity;

import org.springframework.stereotype.Repository;

@Repository
public interface ParkingRepository extends AbstractRepository<ParkingEntity> {

    ParkingEntity findByAddress(String address);
    ParkingEntity findByLatitudeAndLongitude(Double Latitude, Double Longitude);
}

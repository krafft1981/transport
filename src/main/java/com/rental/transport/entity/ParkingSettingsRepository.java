package com.rental.transport.entity;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSettingsRepository extends AbstractRepository<ParkingSettingsEntity> {

    List<ParkingSettingsEntity> findByParkingId(Long id);
}

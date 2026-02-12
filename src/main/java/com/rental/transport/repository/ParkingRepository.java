package com.rental.transport.repository;

import com.rental.transport.entity.ParkingEntity;
import com.rental.transport.repository.template.IRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ParkingRepository extends IRepository<ParkingEntity> {

    List<ParkingEntity> findAllByCustomerId(UUID id);
    List<ParkingEntity> findAllByEnableTrue(Pageable pageable);
}

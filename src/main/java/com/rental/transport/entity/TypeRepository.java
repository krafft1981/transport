package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends IRepository<TypeEntity> {

    TypeEntity findByEnableTrueAndName(String name);
    List<TypeEntity> findAllByEnableTrue(Pageable pageable);
}

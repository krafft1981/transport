package com.rental.transport.entity;

import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends IRepository<TypeEntity> {

    TypeEntity findByEnableTrueAndName(String name);
}

package com.rental.transport.entity;

import org.springframework.stereotype.Repository;

@Repository
public interface TypeRepository extends AbstractRepository<TypeEntity> {

    TypeEntity findTypeByName(String name);
}

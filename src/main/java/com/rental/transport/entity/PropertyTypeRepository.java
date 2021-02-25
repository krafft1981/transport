package com.rental.transport.entity;

public interface PropertyTypeRepository extends IRepository<PropertyTypeEntity> {

    PropertyTypeEntity findByLogicName(String logicName);
}

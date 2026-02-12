package com.rental.transport.repository;

import com.rental.transport.entity.PropertyEntity;
import com.rental.transport.repository.template.IRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PropertyRepository extends IRepository<PropertyEntity> {

}

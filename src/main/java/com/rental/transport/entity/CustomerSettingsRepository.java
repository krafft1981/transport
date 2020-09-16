package com.rental.transport.entity;

import java.util.Map;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerSettingsRepository extends AbstractRepository<CustomerSettingsEntity> {

    @Query("select cs from customer_settings cs where customer_id = :id")
    Map<String, CustomerSettingsEntity> findByCustomerId(Long id);
}

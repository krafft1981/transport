package com.rental.transport.entity;

import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplatesRepository extends IRepository<TemplatesEntity> {

    List<TemplatesEntity> findByTransportTypeId(Long id);

    List<TemplatesEntity> findByTransportTypeIdAndPropertyTypeId(
            Long transportTypeId,
            Long propertyTypeId
    );
}

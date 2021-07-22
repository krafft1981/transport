package com.rental.transport.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends IRepository<ReviewEntity> {

    @Query(
        nativeQuery = true,
        value = "select count(*) from review where transport_id = :transportId"
    )
    Long findCountByTransport(
        @Param("transportId") Long transportId
    );

    @Query(
        nativeQuery = true,
        value = "select sum(score) from review where transport_id = :transportId"
    )
    Long findSumByTransport(
        @Param("transportId") Long transportId
    );

    ReviewEntity findByCustomerAndTransport(CustomerEntity customer, TransportEntity transport);
}

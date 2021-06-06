package com.rental.transport.entity;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends IRepository<RequestEntity> {

    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours " +
                    "from request r where day = :day and r.transport_id = :transportId"
    )
    List<RequestEntity> findRequestByTransportAndDay(
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours " +
                    "from request r where day = :day and r.transport_id = :transportId and status = 'NEW'"
    )
    List<RequestEntity> findNewRequestByTransportAndDay(
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours " +
                    "from request r where status = 'NEW' and driver_id = :customerId"
    )
    List<RequestEntity> findRequestByDriver(
            @Param("customerId") Long customerId
    );

    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours " +
                    "from request r where status = 'NEW' and customer_id = :customerId"
    )
    List<RequestEntity> findRequestByCustomer(
            @Param("customerId") Long customerId
    );

    @Query(
            nativeQuery = true,
            value = "delete from request where status = 'NEW' and customer_id = :customerId and transport_id = :transportId and day = :day"
    )
    @Modifying
    @Transactional
    void deleteByCustomerAndTransportByDay(
            @Param("customerId") Long customerId,
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours " +
                    "from request r where status = 'NEW' and customer_id = :customerId and transport_id = :transportId and day = :day"
    )
    List<RequestEntity> findNewByCustomerAndTransportAndDay(
            @Param("customerId") Long customerId,
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    @Query(
            nativeQuery = true,
            value = "update request set status = 'EXPIRED', interact_at = CURRENT_TIMESTAMP where id in (select id from request where status = 'NEW' and day <= extract(epoch from now()) * 1000)"
    )
    @Modifying
    @Transactional
    void setExpired();
}

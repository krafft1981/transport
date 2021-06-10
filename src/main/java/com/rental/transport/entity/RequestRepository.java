package com.rental.transport.entity;

import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends IRepository<RequestEntity> {

    // Применяется для загрузки календаря по транспорту
    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
                    "where day = :day and r.transport_id = :transportId"
    )
    List<RequestEntity> findRequestByTransportAndDay(
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    // Применяется для загрузки календаря по водителю
    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
                    "where day = :day and r.driver_id = :driverId"
    )
    List<RequestEntity> findNewRequestByDriverAndDay(
            @Param("driverId") Long driverId,
            @Param("day") Long day
    );

    // Применяется для загрузки календаря по пользователю
    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
                    "where day = :day and r.customer_id = :customerId"
    )
    List<RequestEntity> findNewRequestByCustomerAndDay(
            @Param("customerId") Long customerId,
            @Param("day") Long day
    );

    // Прменяется для автоотмены заявок по транспорту
    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
                    "where day = :day and r.transport_id = :transportId and status = 'NEW'"
    )
    List<RequestEntity> findNewRequestByTransportAndDay(
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    // Прменяется для автоотмены заявок по водителю
    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
                    "where status = 'NEW' and driver_id = :customerId"
    )
    List<RequestEntity> findRequestByDriver(
            @Param("customerId") Long customerId
    );

    // Прменяется для автоотмены заявок по пользователю
    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
                    "where status = 'NEW' and customer_id = :customerId"
    )
    List<RequestEntity> findRequestByCustomer(
            @Param("customerId") Long customerId
    );

    // Применяется при обновлении заявки
    @Query(
            nativeQuery = true,
            value = "delete from request " +
                    "where status = 'NEW' and customer_id = :customerId and transport_id = :transportId and day = :day"
    )
    @Modifying
    @Transactional
    void deleteByCustomerAndTransportByDay(
            @Param("customerId") Long customerId,
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    // Применяется при изменении статуса заявки
    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
                    "where status = 'NEW' and customer_id = :customerId and transport_id = :transportId and day = :day"
    )
    List<RequestEntity> updateNewByDay(
            @Param("customerId") Long customerId,
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    // Применяется при автоматическом обновлении статуса заявки на просрочена
    @Query(
            nativeQuery = true,
            value = "update request set status = 'EXPIRED', interact_at = CURRENT_TIMESTAMP " +
                    "where id in (select id from request where status = 'NEW' and Date(to_timestamp(day/1000)) < Date(Now())"
    )
    @Modifying
    @Transactional
    void setExpired();
}

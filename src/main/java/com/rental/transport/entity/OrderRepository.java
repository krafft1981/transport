package com.rental.transport.entity;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends IRepository<OrderEntity> {

    // Поиск заказов по пользователю
    @Query(
            nativeQuery = true,
            value = "select o.id, o.day, o.hours, o.customer_id, o.driver_id, o.transport_id from orders o " +
                    "where customer_id = :customerId and Date(to_timestamp(day/1000)) >= Date(Now())"
    )
    List<OrderEntity> findByCustomer(
            @Param("customerId") Long customerId
    );

    // Поиск заказов по водителю
    @Query(
            nativeQuery = true,
            value = "select o.id, o.day, o.hours, o.customer_id, o.driver_id, o.transport_id from orders o " +
                    "where driver_id = :driverId and Date(to_timestamp(day/1000)) >= Date(Now())"
    )
    List<OrderEntity> findByDriver(
            @Param("driverId") Long driverId
    );

    // Поиск заказов по транспорту
    @Query(
            nativeQuery = true,
            value = "select o.id, o.day, o.hours, o.customer_id, o.driver_id, o.transport_id from orders o " +
                    "where transport_id = :transportId and Date(to_timestamp(day/1000)) >= Date(Now())"
    )
    List<OrderEntity> findByTransport(
            @Param("transportId") Long transportId
    );

    // Поиск заказов по заказчику
    @Query(
            nativeQuery = true,
            value = "select o.id, o.day, o.hours, o.customer_id, o.driver_id, o.transport_id from orders o " +
                    "where customer_id = :customerId and day = :day"
    )
    List<OrderEntity> findByCustomerAndDay(
            @Param("customerId") Long customerId,
            @Param("day") Long day
    );

    // Поиск заказов по Водителю и дню
    List<OrderEntity> findByDriverAndDay(CustomerEntity driver, Long day);
}

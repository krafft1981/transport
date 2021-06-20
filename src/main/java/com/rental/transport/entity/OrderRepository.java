package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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

    List<OrderEntity> findByCustomerAndTransportAndDay(CustomerEntity customer, TransportEntity transport, Long day);
    List<OrderEntity> findByDriverAndDay(CustomerEntity driver, Long day);
}

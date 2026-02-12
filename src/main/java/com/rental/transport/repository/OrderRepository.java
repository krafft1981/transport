package com.rental.transport.repository;

import com.rental.transport.entity.OrderEntity;
import com.rental.transport.repository.template.IRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends IRepository<OrderEntity> {

//    // Поиск заказов по пользователю
//    @Query(
//            nativeQuery = true,
//            value = "select o.id, o.day, o.hours, o.customer_id, o.driver_id, o.transport_id from orders o " +
//                    "where customer_id = :customerId and Date(to_timestamp(day/1000)) >= (select Date((Now()) at time zone time_zone) from customer where id = customer_id)"
//    )
//    List<OrderEntity> findByCustomer(
//            @Param("customerId") UUID customerId
//    );
//
//    // Поиск заказов по водителю
//    @Query(
//            nativeQuery = true,
//            value = "select o.id, o.day, o.hours, o.customer_id, o.driver_id, o.transport_id from orders o " +
//                    "where driver_id = :driverId and Date(to_timestamp(day/1000)) >= (select Date((Now()) at time zone time_zone) from customer where id = customer_id)"
//    )
//    List<OrderEntity> findByDriver(
//            @Param("driverId") UUID driverId
//    );
//
//    // Поиск заказов по транспорту
//    @Query(
//            nativeQuery = true,
//            value = "select o.id, o.day, o.hours, o.customer_id, o.driver_id, o.transport_id from orders o " +
//                    "where transport_id = :transportId and Date(to_timestamp(day/1000)) >= (select Date((Now()) at time zone time_zone) from customer where id = customer_id)"
//    )
//    List<OrderEntity> findByTransport(
//            @Param("transportId") UUID transportId
//    );

//    List<OrderEntity> findByCustomerAndTransportAndDay(CustomerEntity customer, TransportEntity transport, Long day);
//    List<OrderEntity> findByDriverAndDay(CustomerEntity driver, Long day);
}

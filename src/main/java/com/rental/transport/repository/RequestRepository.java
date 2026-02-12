package com.rental.transport.repository;

import com.rental.transport.entity.RequestEntity;
import com.rental.transport.repository.template.IRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends IRepository<RequestEntity> {

//    @Query(
//            nativeQuery = true,
//            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
//                    "where status = 'NEW' and day = :day and r.customer_id = :customerId"
//    )
//    List<RequestEntity> findNewByCustomerAndDay(
//            @Param("customerId") UUID customerId,
//            @Param("day") Long day
//    );
//
//    @Query(
//            nativeQuery = true,
//            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
//                    "where status = 'NEW' and day = :day and driver_id = :customerId"
//    )
//    List<RequestEntity> findNewByDriverAndDay(
//            @Param("customerId") UUID customerId,
//            @Param("day") Long day
//    );
//
//    @Query(
//            nativeQuery = true,
//            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
//                    "where status = 'NEW' and day = :day and customer_id = :customerId and transport_id = :transportId"
//    )
//    List<RequestEntity> findNewByCustomerAndTransportAndDay(
//            @Param("customerId") UUID customerId,
//            @Param("transportId") UUID transportId,
//            @Param("day") Long day
//    );
//
//    @Query(
//            nativeQuery = true,
//            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
//                    "where status = 'NEW' and transport_id = :transportId"
//    )
//    List<RequestEntity> findNewByTransport(
//            @Param("transportId") UUID transportId
//    );
//
//    @Query(
//            nativeQuery = true,
//            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
//                    "where status = 'NEW' and customer_id = :customerId"
//    )
//    List<RequestEntity> findNewByCustomer(
//            @Param("customerId") UUID customerId
//    );
//
//    @Query(
//            nativeQuery = true,
//            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
//                    "where status = 'NEW' and driver_id = :driverId"
//    )
//    List<RequestEntity> findNewByDriver(
//            @Param("driverId") UUID driverId
//    );
//
//    @Query(
//            nativeQuery = true,
//            value = "delete from request " +
//                    "where status = 'NEW' and customer_id = :customerId and transport_id = :transportId and day = :day"
//    )
//    @Modifying
//    @Transactional
//    void deleteByCustomerAndTransportByDay(
//            @Param("customerId") UUID customerId,
//            @Param("transportId") UUID transportId,
//            @Param("day") Long day
//    );
//
//    @Query(
//            nativeQuery = true,
//            value = "select r.id, r.created_at, r.interact_at, r.order_id, r.status, r.customer_id, r.driver_id, r.transport_id, r.day, r.hours from request r " +
//                    "where status = 'NEW' and customer_id = :customerId and transport_id = :transportId and day = :day"
//    )
//    List<RequestEntity> findToUpdateNewByDay(
//            @Param("customerId") UUID customerId,
//            @Param("transportId") UUID transportId,
//            @Param("day") Long day
//    );
//
//    @Query(
//            nativeQuery = true,
//            value = "update request set status = 'EXPIRED', interact_at = CURRENT_TIMESTAMP where " +
//                        "id in (select id from request where " +
//                            "status = 'NEW' and " +
//                            "Date(to_timestamp(day/1000)) < (select Date((Now()) at time zone time_zone) from customer where id = customer_id)" +
//                        ")"
//    )
//    @Modifying
//    @Transactional
//    void setExpiredByDay();
//
//    List<RequestEntity> findByCustomerAndTransportAndDayAndStatus(CustomerEntity customer, TransportEntity transport, Long day, RequestStatusEnum status);
}

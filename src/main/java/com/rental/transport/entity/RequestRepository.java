package com.rental.transport.entity;

import com.rental.transport.enums.RequestStatusEnum;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends IRepository<RequestEntity> {

    @Query(
            nativeQuery = true,
            value = "select r.id, r.created_At, r.interact_at, r.order_id, r.status, r.driver_id, r.transport_id, r.calendar_id, r.customer_id from request r where " +
                    "transport_id = :transportId " +
                    "and calendar_id in (select id from calendar where day_num = :day)"
    )
    List<RequestEntity> findByTransportAndDay(
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    @Query("delete from request where status = 'NEW' " +
            "and customer_id = :customerId " +
            "and transport_id = :transportId " +
            "and calendar_id in (select id from calendar where day_num = :day)"
    )
    @Modifying
    void deleteByCustomerAndTransportByDay(
            @Param("customerId") Long customerId,
            @Param("transportId") Long transportId,
            @Param("day") Long day
    );

    @Query("update request set status = 'EXPIRED' where status = 'NEW' " +
            "and interact_at is null " +
            "and calendar_id in (select id from calendar where day_num < :day or ((day_num = :day) and hour <= :hour))"
    )
    @Transactional
    @Modifying
    void setExpired(
            @Param("day") Long day,
            @Param("hour") Integer hour
    );


    List<RequestEntity> findByCustomerAndTransportAndDriverAndCalendarAndStatus(
            CustomerEntity customer,
            TransportEntity transport,
            CustomerEntity driver,
            CalendarEntity calendar,
            RequestStatusEnum status
    );

    List<RequestEntity> findByCustomerAndStatus(
            CustomerEntity customer,
            RequestStatusEnum status
    );

    List<RequestEntity> findByCustomer(CustomerEntity customer, Pageable pageable);

    List<RequestEntity> findByDriver(CustomerEntity driver, Pageable pageable);

    List<RequestEntity> findByCustomerAndTransportAndCalendarAndStatus(
            CustomerEntity customer,
            TransportEntity transport,
            CalendarEntity calendar,
            RequestStatusEnum status
    );
}

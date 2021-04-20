package com.rental.transport.entity;

import com.rental.transport.enums.RequestStatusEnum;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends IRepository<RequestEntity> {

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

    List<RequestEntity> findByTransportAndStatus(
            TransportEntity transport,
            RequestStatusEnum status
    );

    List<RequestEntity> findByCustomerAndTransportAndDriverAndCalendarAndStatus(
            CustomerEntity customer,
            TransportEntity transport,
            CustomerEntity driver,
            CalendarEntity calendar,
            RequestStatusEnum status
    );

    @Query("delete from request where " +
                    "status = 'New' " +
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
}

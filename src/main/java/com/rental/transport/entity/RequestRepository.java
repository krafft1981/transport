package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends IRepository<RequestEntity> {

    List<RequestEntity> getByCustomerId(Long customerId);
    List<RequestEntity> getByCustomerId(Long customerId, Pageable pageable);

    List<RequestEntity> getByDriverId(Long driverId);
    List<RequestEntity> getByDriverId(Long driverId, Pageable pageable);

    void deleteByCalendarId(Long calendarId);

    RequestEntity getByCustomerAndDriverAndTransportAndCalendar(
            CustomerEntity customer,
            CustomerEntity driver,
            TransportEntity transport,
            CalendarEntity calendar
    );

    List<RequestEntity> getByCustomerAndTransportAndCalendar(
            CustomerEntity customer,
            TransportEntity transport,
            CalendarEntity calendar
    );
}

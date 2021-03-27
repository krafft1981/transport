package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface RequestRepository extends IRepository<RequestEntity> {

    List<RequestEntity> findByCustomerAndInteractAtNull(CustomerEntity customer);
    List<RequestEntity> findByCustomer(CustomerEntity customer, Pageable pageable);
    List<RequestEntity> findByDriver(CustomerEntity driver, Pageable pageable);

    List<RequestEntity> findByCustomerAndTransportAndCalendarAndInteractAtNull(
            CustomerEntity customer,
            TransportEntity transport,
            CalendarEntity calendar
    );

    List<RequestEntity> findByTransportAndInteractAtNull(TransportEntity transport);

    List<RequestEntity> findByCustomerAndTransportAndDriverAndCalendarAndInteractAtNull(
            CustomerEntity customer,
            TransportEntity transport,
            CustomerEntity driver,
            CalendarEntity calendar
    );
}

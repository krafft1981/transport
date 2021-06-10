package com.rental.transport.entity;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteBookRepository extends IRepository<NoteBookEntity> {

    @Query(
            nativeQuery = true,
            value = "select n.id, n.date, n.text, n.calendar_id, n.customer_id from notebook n " +
                    "where customer_id = :customerId"
    )
    List<RequestEntity> findByCustomerAndDay(
            @Param("customerId") Long customerId
//            @Param("day") Long day
    );
}

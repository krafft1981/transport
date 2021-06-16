package com.rental.transport.service;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Text;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteBookService {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private CustomerService customerService;

    @Transactional
    public Event postAbsentCustomerEntry(String account, Long day, Integer[] hours)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        calendarService.checkCustomerBusy(customer, day, hours);
        CalendarEntity calendar = calendarService.getEntity(day, hours);
        customer.addCalendar(calendar);
        return new Event(EventTypeEnum.NOTE, day, hours);
    }

    @Transactional
    public Long putAbsentCustomerEntry(String account, Long id, Text body) {

        CustomerEntity customer = customerService.getEntity(account);

        return 0L;
    }

    @Transactional
    public void deleteAbsentCustomerEntry(String account, Long[] ids)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        for (Long id : ids) {
            CalendarEntity calendar = calendarService.getEntity(id);
            customer.deleteCalendarEntity(calendar);
        }
    }

}

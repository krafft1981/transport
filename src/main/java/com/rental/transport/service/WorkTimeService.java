package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.BooleanYesValidator;
import com.rental.transport.utils.validator.IStringValidator;
import java.util.GregorianCalendar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkTimeService {

    @Autowired
    private PropertyService propertyService;

    private Calendar getCustomerWorkTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException, IllegalArgumentException {

        Integer startWorkAt = Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_startWorkTime"));
        Integer stopWorkAt = Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_stopWorkTime"));
        return new Calendar(day, startWorkAt, stopWorkAt);
    }

    private Calendar getCustomerHolidayTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException {

        String workAtWeekEnd = propertyService.getValue(customer.getProperty(), "customer_workAtWeekEnd");

        IStringValidator validator = new BooleanYesValidator();
        if (validator.validate(workAtWeekEnd))
            return getCustomerWorkTime(day, customer);

        return new Calendar(day);
    }

    public Calendar getCustomerWeekTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException, IllegalArgumentException {

        java.util.Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(day);

        switch (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            case java.util.Calendar.MONDAY:
            case java.util.Calendar.TUESDAY:
            case java.util.Calendar.WEDNESDAY:
            case java.util.Calendar.THURSDAY:
            case java.util.Calendar.FRIDAY: {
                return getCustomerWorkTime(day, customer);
            }
            case java.util.Calendar.SATURDAY:
            case java.util.Calendar.SUNDAY: {
                return getCustomerHolidayTime(day, customer);
            }
        }

        return new Calendar(day);
    }
}

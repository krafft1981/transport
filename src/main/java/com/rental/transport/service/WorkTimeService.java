package com.rental.transport.service;

import com.rental.transport.dto.Event;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.BooleanYesValidator;
import com.rental.transport.utils.validator.IStringValidator;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkTimeService {

    @Autowired
    private PropertyService propertyService;

    private List<Event> getCustomerWorkTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException {

        List<Event> result = new ArrayList<>();
        result.add(new Event(EventTypeEnum.GENERATED, day, 0, Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_startWorkTime"))));
        result.add(new Event(EventTypeEnum.FREE, day,
                Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_startWorkTime")),
                Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_stopWorkTime")))
        );
        result.add(new Event(EventTypeEnum.GENERATED, day, Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_stopWorkTime")), 24));
        return result;
    }

    private List<Event> getCustomerHolidayTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException {

        IStringValidator validator = new BooleanYesValidator();
        if (validator.validate(propertyService.getValue(customer.getProperty(), "customer_workAtWeekEnd")))
            return getCustomerWorkTime(day, customer);

        List<Event> result = new ArrayList<>();
        result.add(new Event(EventTypeEnum.GENERATED, day, 0, 24));
        return result;
    }

    public List<Event> getCustomerWeekTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException {

        java.util.Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(day);

        switch (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            case java.util.Calendar.MONDAY:
            case java.util.Calendar.TUESDAY:
            case java.util.Calendar.WEDNESDAY:
            case java.util.Calendar.THURSDAY:
            case java.util.Calendar.FRIDAY: {
                return getCustomerWorkTime(day, customer);
            }
            default: {
                return getCustomerHolidayTime(day, customer);
            }
        }
    }
}

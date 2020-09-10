package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.mapper.CalendarMapper;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    @Autowired
    private CalendarMapper mapper;

    @Autowired
    private CalendarRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    public Long count() {

        Long count = repository.count();
        return count;
    }

    public void update(String account, Long[] hours) {

    }

    public List<Calendar> getEvents(String account, Date start, Date stop) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        List<Calendar> entities = repository
                .getEntityByCustomerIdUseStartAndStop(customer.getId(), start, stop)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());

        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(start);
        System.out.println(calendar.getTimeZone() + " " + calendar.getWeekYear());

        if ((customer.getStartWorkAt() != null) && (customer.getStopWorkAt() != null)) {

//            new SimpleDateFormat("yyyy-MM-dd HH").parse(stop));
//            new SimpleDateFormat("yyyy-MM-dd HH").parse(stop));
        }

        if (customer.getWorkAtWeekEnd() == false) {

        }

        return entities;
    }
}

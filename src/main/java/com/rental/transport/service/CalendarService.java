package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.mapper.CalendarMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.TimeZone;
import java.util.stream.Collectors;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    @Autowired
    private CalendarMapper mapper;

    @Autowired
    private TransportService transportService;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Long count() {

        return calendarRepository.count();
    }

    @Getter
    public class Diapazon {

        private Date start;
        private Date stop;
        private Long dayNum;

        private static final String message = "Wrong time diapazone";

        public Diapazon(Long day, Date start, Date stop) throws IllegalArgumentException {

            this(day);
        }

        public Diapazon(Long day) throws IllegalArgumentException {

            GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            gregorianCalendar.setTimeInMillis(day);
            gregorianCalendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            gregorianCalendar.set(java.util.Calendar.MINUTE, 0);
            gregorianCalendar.set(java.util.Calendar.SECOND, 0);
            gregorianCalendar.set(java.util.Calendar.MILLISECOND, 0);

            this.start = gregorianCalendar.getTime();
            this.dayNum = start.getTime();

            gregorianCalendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
            this.stop = gregorianCalendar.getTime();
        }
    }

    public CalendarEntity getEntityById(Long id) throws ObjectNotFoundException {

        return calendarRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Diapazone", id));
    }

    public void putOutEvent(String account, Long day, Date start, Date stop)
            throws IllegalArgumentException {

        Diapazon diapazon = new Diapazon(day, start, stop);

        CustomerEntity customer = customerRepository.findByAccount(account);
        checkCustomerBusy(customer, day, start, stop);

        CalendarEntity entity = new CalendarEntity(
                start,
                stop,
                day,
                null,
                customer);

        calendarRepository.save(entity);
    }

    public void deleteOutEvent(String account, Long day, Date start, Date stop)
            throws IllegalArgumentException {

        Diapazon diapazon = new Diapazon(day, start, stop);

        CustomerEntity customer = customerRepository.findByAccount(account);
        calendarRepository.deleteByCustomerIdAndDayAndStartAndStop(customer.getId(), day, start, stop);
    }

    private void checkTimeDiapazon(Long calendarStart, Long calendarStop, Long tryStart, Long tryStop)
            throws IllegalArgumentException {

        final String message = "Time is busy";

        if ((tryStart <= calendarStart) && (tryStop >= calendarStop))
            throw new IllegalArgumentException(message);

        if ((tryStart > calendarStart) && (tryStop < calendarStart))
            throw new IllegalArgumentException(message);

        if ((tryStart >= calendarStart) && (tryStart < calendarStop))
            throw new IllegalArgumentException(message);

        if ((tryStop >= calendarStart) && (tryStop < calendarStop))
            throw new IllegalArgumentException(message);
    }

    public void checkCustomerBusy(CustomerEntity customer, Long day, Date start, Date stop)
            throws IllegalArgumentException {

        Diapazon diapazon = new Diapazon(day, start, stop);

        getCustomerEventList(customer, day)
                .stream()
                .forEach(entity -> checkTimeDiapazon(
                        entity.getStartAt(),
                        entity.getStopAt(),
                        start.getTime(),
                        stop.getTime()
                ));
    }

    public List<Calendar> getDayEventList(String account, Long day) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        return getCustomerEventList(customer, day);
    }

    public Boolean checkTransportBusy(TransportEntity transport, Long day, Date start, Date stop)
            throws IllegalArgumentException {

        transport
                .getCustomer()
                .stream()
                .forEach(customer -> checkCustomerBusy(customer, day, start, stop));

        return true;
    }

    public List<Calendar> getTransportEventList(Long id, Long day)
            throws ObjectNotFoundException {

        return transportService
                .get(id)
                .getCustomer()
                .stream()
                .map(customer -> getCustomerEventList(customer, day))
                .flatMap(events -> events.stream())
                .collect(Collectors.toList());
    }

    private List<Calendar> getCustomerHolidayTime(CustomerEntity customer, Diapazon diapazon) {

        Boolean workAtWeekEnd = null;//Boolean.getBoolean(propertyService.getValue(customer.getProperty(), "work_at_week_end"));
        if (workAtWeekEnd) {
            return getCustomerWorkTime(customer, diapazon);
        }

        List<Calendar> events = new ArrayList<>();

        events.add(new Calendar(
                diapazon.getStart().getTime(),
                diapazon.getStop().getTime(),
                diapazon.getDayNum()
        ));

        return events;
    }

    private List<Calendar> getCustomerWorkTime(CustomerEntity customer, Diapazon diapazon)
            throws ObjectNotFoundException {

        List<Calendar> events = new ArrayList<>();

        Integer startWorkAt = null;//Integer.getInteger(propertyService.getValue(customer.getProperty(), "startWorkAt"));
        Integer stopWorkAt  = null;//Integer.getInteger(propertyService.getValue(customer.getProperty(), "stopWorkAt"));

        if (!startWorkAt.equals(stopWorkAt)) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            gregorianCalendar.setTime(diapazon.getStart());
            gregorianCalendar.set(java.util.Calendar.HOUR_OF_DAY, startWorkAt);
            gregorianCalendar.set(java.util.Calendar.MINUTE, 0);
            gregorianCalendar.set(java.util.Calendar.SECOND, 0);
            gregorianCalendar.set(java.util.Calendar.MILLISECOND, 0);

            events.add(new Calendar(
                    diapazon.getStart().getTime(),
                    gregorianCalendar.getTimeInMillis(),
                    diapazon.getDayNum()
            ));

            gregorianCalendar.set(java.util.Calendar.HOUR_OF_DAY, stopWorkAt);

            events.add(new Calendar(
                    gregorianCalendar.getTimeInMillis(),
                    diapazon.getStop().getTime(),
                    diapazon.getDayNum()
            ));
        }

        return events;
    }

    private List<Calendar> getCustomerEventList(CustomerEntity customer, Long day)
            throws IllegalArgumentException {

        Diapazon diapazon = new Diapazon(day);

        List<Calendar> events = calendarRepository
                .findByCustomerIdAndDayAndStartAndStop(
                        customer.getId(),
                        day,
                        diapazon.getStart(),
                        diapazon.getStop()
                )
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(day);

        switch (gregorianCalendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            case java.util.Calendar.MONDAY:
            case java.util.Calendar.TUESDAY:
            case java.util.Calendar.WEDNESDAY:
            case java.util.Calendar.THURSDAY:
            case java.util.Calendar.FRIDAY: {
                events.addAll(getCustomerWorkTime(customer, diapazon));
                break;
            }
            case java.util.Calendar.SATURDAY:
            case java.util.Calendar.SUNDAY: {
                events.addAll(getCustomerHolidayTime(customer, diapazon));
                break;
            }
        }

        return events;
    }
}

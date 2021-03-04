package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.TransportRepository;
import com.rental.transport.mapper.CalendarMapper;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.validator.BooleanYesValidator;
import com.rental.transport.validator.IStringValidator;
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
    private CalendarMapper calendarMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TransportService transportService;

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private TransportRepository transportRepository;

    @Getter
    public class Diapazon {

        private Date start;
        private Date stop;
        private Long dayNum;

        private static final String message = "Wrong time diapazone";

        public Diapazon(Long day) throws IllegalArgumentException {

            java.util.Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            calendar.setTimeInMillis(day);
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);

            this.start = calendar.getTime();
            this.dayNum = start.getTime();

            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
            this.stop = calendar.getTime();
        }
    }

    public Long putBusy(String account, Long day, Date start, Date stop)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        checkCustomerBusy(customer, day, start, stop);
        CalendarEntity entity = new CalendarEntity(start, stop, day, customer);
        return calendarRepository.save(entity).getId();
    }

    public void deleteBusy(String account, Long day, Date start, Date stop)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        calendarRepository
                .findByDayAndStartAndStop(day, start, stop)
                .stream()
                .filter(entity -> entity.getCustomer().getId().equals(customer.getId()))
                .forEach(entity -> {
                    calendarRepository.deleteById(entity.getId());
                });
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

    private void checkCustomerBusy(CustomerEntity customer, Long day, Date start, Date stop)
            throws IllegalArgumentException {

        getCustomerCalendar(customer, day)
                .stream()
                .forEach(
                        entity -> checkTimeDiapazon(
                                entity.getStartAt(),
                                entity.getStopAt(),
                                start.getTime(),
                                stop.getTime()
                        )
                );
    }

    public List<Calendar> getTransportCalendar(Long id, Long day) {

        return new ArrayList<>();

//        return transportService
//                .getEntity(id)
//                .getCalendar()
//                .stream()
//                .map(entity -> {return entity;})
//                .collect(Collectors.toList());
    }

    public List<Calendar> getCustomerCalendar(CustomerEntity customer, Long day) {

        Diapazon diapazon = new Diapazon(day);

        List<Calendar> res = calendarRepository
                .findByDayAndStartAndStop(
                        day,
                        diapazon.getStart(),
                        diapazon.getStop()
                )
                .stream()
                .map(entity -> {
                    return calendarMapper.toDto(entity);
                })
                .collect(Collectors.toList());



        java.util.Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(day);

        switch (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            case java.util.Calendar.MONDAY:
            case java.util.Calendar.TUESDAY:
            case java.util.Calendar.WEDNESDAY:
            case java.util.Calendar.THURSDAY:
            case java.util.Calendar.FRIDAY: {
                res.addAll(getCustomerWorkTime(customer, diapazon));
                break;
            }
            case java.util.Calendar.SATURDAY:
            case java.util.Calendar.SUNDAY: {
                res.addAll(getCustomerHolidayTime(customer, diapazon));
                break;
            }
        }

        return res;
    }

    public List<Calendar> getCustomerCalendar(String account, Long day)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return getCustomerCalendar(customer, day);
    }

    public List<Calendar> getTransportCalendar(String account, Long day, Long transportId)
            throws ObjectNotFoundException {

        CustomerEntity customerId = customerService.getEntity(account);
        List<Calendar> customerEvents = getCustomerCalendar(customerId, day);
        List<Calendar> transportEvents = getTransportCalendar(transportId, day);

        // TODO release mixer
        return new ArrayList<>();
    }

    private List<Calendar> getCustomerWorkTime(CustomerEntity customer, Diapazon diapazon)
            throws ObjectNotFoundException, IllegalArgumentException, NumberFormatException {

        String startWorkAt = propertyService.getValue(customer.getProperty(), "startWorkTime");
        String stopWorkAt = propertyService.getValue(customer.getProperty(), "stopWorkTime");

        java.util.Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.setTime(diapazon.getStart());
        calendar.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(startWorkAt));
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);

        List<Calendar> events = new ArrayList<>();

        events.add(new Calendar(
                diapazon.getStart().getTime(),
                calendar.getTimeInMillis(),
                diapazon.getDayNum(),
                customerMapper.toDto(customer)
        ));

        calendar.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(stopWorkAt));

        events.add(new Calendar(
                calendar.getTimeInMillis(),
                diapazon.getStop().getTime(),
                diapazon.getDayNum(),
                customerMapper.toDto(customer)
        ));

        return events;
    }

    private List<Calendar> getCustomerCalendar(CustomerEntity customer, Long[] days)
            throws IllegalArgumentException {

        List<Calendar> res = new ArrayList<>();
        for (Long day : days)
            res.addAll(getCustomerCalendar(customer, day));

        return res;
    }

    private List<Calendar> getCustomerHolidayTime(CustomerEntity customer, Diapazon diapazon)
            throws ObjectNotFoundException {

        String workAtWeekEnd = propertyService.getValue(customer.getProperty(), "workAtWeekEnd");

        IStringValidator validator = new BooleanYesValidator();
        if (validator.validate(workAtWeekEnd))
            return getCustomerWorkTime(customer, diapazon);

        List<Calendar> events = new ArrayList<>();

        events.add(new Calendar(
                diapazon.getStart().getTime(),
                diapazon.getStop().getTime(),
                diapazon.getDayNum(),
                customerMapper.toDto(customer)
        ));

        return events;
    }

    public CalendarEntity getEntity(Long id) throws ObjectNotFoundException {

        return calendarRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Calendar", id));
    }

    public Calendar getDto(Long id) throws ObjectNotFoundException {

        return calendarMapper.toDto(getEntity(id));
    }

    public Long count() {
        return calendarRepository.count();
    }
}

package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
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
    private CalendarRepository calendarRepository;

    @Autowired
    private CustomerService customerService;

    @Getter
    public class Diapazon {

        private Date start;
        private Date stop;
        private Long dayNum;

        private static final String message = "Wrong time diapazone";

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

        return transportService
                .getEntity(id)
                .getCustomer()
                .stream()
                .map(customer -> {
                    return getCustomerCalendar(customer, day);
                })
                .flatMap(customer -> customer.stream())
                .collect(Collectors.toList());
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

        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTimeInMillis(day);

        switch (gregorianCalendar.get(java.util.Calendar.DAY_OF_WEEK)) {
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

    public List<Calendar> getCustomerCalendar(String account, Long[] day)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return getCustomerCalendar(customer, day);
    }

    public List<Calendar> getTransportCalendar(String account, Long id, Long[] days)
            throws ObjectNotFoundException {

        List<Calendar> res = new ArrayList<>();
        for (Long day : days)
            res.addAll(getTransportCalendar(id, day));
        return res;
    }

    private List<Calendar> getCustomerWorkTime(CustomerEntity customer, Diapazon diapazon)
            throws ObjectNotFoundException, IllegalArgumentException, NumberFormatException {

        String startWorkAt = propertyService.getValue(customer.getProperty(), "startWorkTime");
        String stopWorkAt = propertyService.getValue(customer.getProperty(), "stopWorkTime");

        GregorianCalendar gregorianCalendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        gregorianCalendar.setTime(diapazon.getStart());
        gregorianCalendar.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(startWorkAt));
        gregorianCalendar.set(java.util.Calendar.MINUTE, 0);
        gregorianCalendar.set(java.util.Calendar.SECOND, 0);
        gregorianCalendar.set(java.util.Calendar.MILLISECOND, 0);

        List<Calendar> events = new ArrayList<>();

        events.add(new Calendar(
                diapazon.getStart().getTime(),
                gregorianCalendar.getTimeInMillis(),
                diapazon.getDayNum(),
                customerMapper.toDto(customer)
        ));

        gregorianCalendar.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(stopWorkAt));

        events.add(new Calendar(
                gregorianCalendar.getTimeInMillis(),
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

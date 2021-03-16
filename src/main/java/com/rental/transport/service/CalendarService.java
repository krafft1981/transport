package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.CalendarEventTypeEnum;
import com.rental.transport.mapper.CalendarMapper;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.BooleanYesValidator;
import com.rental.transport.utils.validator.IStringValidator;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;
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
    private PropertyService propertyService;

    @Autowired
    private TransportService transportService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CalendarRepository calendarRepository;

    @Getter
    public class Diapazon {

        private Date start;
        private Date stop;

        private Long day;

        public Diapazon(Long day) throws IllegalArgumentException {

            java.util.Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
            calendar.setTimeInMillis(day);
            calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
            calendar.set(java.util.Calendar.MINUTE, 0);
            calendar.set(java.util.Calendar.SECOND, 0);
            calendar.set(java.util.Calendar.MILLISECOND, 0);

            this.start = calendar.getTime();
            this.day = start.getTime();

            calendar.add(java.util.Calendar.DAY_OF_MONTH, 1);
            calendar.add(java.util.Calendar.MILLISECOND, -1);

            this.stop = calendar.getTime();
        }
    }

    public Long putAbsentEntry(String account, Long day, Long start, Long stop)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        checkCustomerBusy(customer, day, start, stop);
        CalendarEntity entity = getEntity(day, start, stop, true);
        customer.addCalendar(entity);
        return entity.getId();
    }

    public void deleteAbsentEntry(String account, Long day, Long start, Long stop)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        CalendarEntity entity = getEntity(day, start, stop, false);
        customer.deleteCalendarEntity(entity);
    }

    public CalendarEntity putOrderEntry(CustomerEntity customer, TransportEntity transport, Long day, Long start, Long stop)
            throws IllegalArgumentException, ObjectNotFoundException {

        checkCustomerBusy(customer, day, start, stop);
        checkTransportBusy(transport, day, start, stop);
        return getEntity(day, start, stop, true);
    }

    public CalendarEntity getEntity(Long day, Long start, Long stop, Boolean create)
            throws ObjectNotFoundException {

        CalendarEntity entity = calendarRepository.findByDayNumAndStartAtAndStopAt(day, new Date(start), new Date(stop));
        if (Objects.isNull(entity)) {
            if (create)
                entity = new CalendarEntity(day, start, stop);
            else {
                String calendarName = String.format("day(%s) start(%s) stop(%s)", day, start, stop);
                throw new ObjectNotFoundException("Calendar", calendarName);
            }
        }

        return entity;
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

    public void checkTransportBusy(TransportEntity transport, Long day, Long start, Long stop)
            throws IllegalArgumentException {

        calendarRepository
                .findTransportCalendarByDay(transport.getId(), day)
                .stream()
                .forEach(
                        entity -> checkTimeDiapazon(
                                entity.getStartAt().getTime(),
                                entity.getStopAt().getTime(),
                                start,
                                stop
                        )
                );
    }

    public void checkCustomerBusy(CustomerEntity customer, Long day, Long start, Long stop)
            throws IllegalArgumentException {

        calendarRepository
                .findCustomerCalendarByDay(customer.getId(), day)
                .stream()
                .forEach(
                        entity -> checkTimeDiapazon(
                                entity.getStartAt().getTime(),
                                entity.getStopAt().getTime(),
                                start,
                                stop
                        )
                );
    }

    private List<Calendar> getCustomerWorkTime(CustomerEntity customer, Diapazon diapazon)
            throws ObjectNotFoundException, IllegalArgumentException {

        String startWorkAt = propertyService.getValue(customer.getProperty(), "customer_startWorkTime");
        String stopWorkAt = propertyService.getValue(customer.getProperty(), "customer_stopWorkTime");

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
                diapazon.getDay(),
                customerMapper.toDto(customer),
                CalendarEventTypeEnum.GENERATED
        ));

        calendar.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(stopWorkAt));

        events.add(new Calendar(
                calendar.getTimeInMillis(),
                diapazon.getStop().getTime(),
                diapazon.getDay(),
                customerMapper.toDto(customer),
                CalendarEventTypeEnum.GENERATED
        ));

        return events;
    }

    public List<Calendar> getCustomerCalendar(String account, Long day)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return getCustomerCalendar(customer, day);
    }

    private List<Calendar> getCustomerHolidayTime(CustomerEntity customer, Diapazon diapazon)
            throws ObjectNotFoundException {

        String workAtWeekEnd = propertyService.getValue(customer.getProperty(), "customer_workAtWeekEnd");

        IStringValidator validator = new BooleanYesValidator();
        if (validator.validate(workAtWeekEnd))
            return getCustomerWorkTime(customer, diapazon);

        List<Calendar> events = new ArrayList<>();

        events.add(new Calendar(
                diapazon.getStart().getTime(),
                diapazon.getStop().getTime(),
                diapazon.getDay(),
                customerMapper.toDto(customer),
                CalendarEventTypeEnum.GENERATED
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

    public List<Calendar> getTransportCalendar(TransportEntity transport, Long day) {

        return calendarRepository.findTransportCalendarByDay(transport.getId(), day)
                .stream()
                .map(entity -> {
                    Calendar dto = calendarMapper.toDto(entity);

//                    if (entity.getOrder() == null)
//                        dto.setType(CalendarEntityTypeEnum.UNAVAILABLE.getId());
//                    else
//                        dto.setType(CalendarEntityTypeEnum.ORDER.getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public List<Calendar> getCustomerCalendar(CustomerEntity customer, Long day) {

        Diapazon diapazon = new Diapazon(day);
        List<Calendar> res = calendarRepository.findCustomerCalendarByDay(customer.getId(), day)
                .stream()
                .map(entity -> {
                    Calendar dto = calendarMapper.toDto(entity);
//                    if (Objects.isNull(entity.getOrder()))
//                        dto.setType(CalendarEntityTypeEnum.UNAVAILABLE.getId());
//                    else
//                        dto.setType(CalendarEntityTypeEnum.ORDER.getId());
                    return dto;
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

    public List<Calendar> getTransportCalendar(String account, Long day, Long transportId)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);

        List<Calendar> customerEvents = getCustomerCalendar(customer, day);
        List<Calendar> transportEvents = getTransportCalendar(transport, day);

        // TODO release mixer
        List<Calendar> events = customerEvents
                .stream()
                .map(event -> {
                    return event;
                })
                .collect(Collectors.toList());

        transportEvents
                .stream()
                .forEach(event -> {

                });

        transport
                .getCustomer()
                .stream()
                .forEach(entity -> {

                });

        return events;
    }
}

package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.dto.Event;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.mapper.CalendarMapper;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.mapper.OrderMapper;
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
    private OrderMapper orderMapper;

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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ConfirmationService confirmationService;

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

    public Long putAbsentCustomerEntry(String account, Long day, Long start, Long stop)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        checkCustomerBusy(customer, day, start, stop);
        CalendarEntity entity = getEntity(day, start, stop, true);
        customer.addCalendar(entity);
        return entity.getId();
    }

    public void deleteAbsentCustomerEntry(String account, Long day, Long start, Long stop)
            throws IllegalArgumentException, ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        CalendarEntity entity = getEntity(day, start, stop, false);
        customer.deleteCalendarEntity(entity);
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
                .forEach(entity -> {
                            System.out.println("Check transport busy");
                            checkTimeDiapazon(
                                    entity.getStartAt().getTime(),
                                    entity.getStopAt().getTime(),
                                    start,
                                    stop
                            );
                            System.out.println("transport not busy");
                        }
                );
    }

    public void checkCustomerBusy(CustomerEntity customer, Long day, Long start, Long stop)
            throws IllegalArgumentException {

        calendarRepository
                .findCustomerCalendarByDay(customer.getId(), day)
                .stream()
                .forEach(entity ->
                        checkTimeDiapazon(
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
                diapazon.getDay(),
                diapazon.getStart().getTime(),
                calendar.getTimeInMillis()
        ));

        calendar.set(java.util.Calendar.HOUR_OF_DAY, Integer.parseInt(stopWorkAt));

        events.add(new Calendar(
                diapazon.getDay(),
                calendar.getTimeInMillis(),
                diapazon.getStop().getTime()
        ));

        return events;
    }

    private List<Calendar> getCustomerWeekTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException, IllegalArgumentException {

        Diapazon diapazon = new Diapazon(day);

        java.util.Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(diapazon.day);

        List<Calendar> res = new ArrayList();
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
        return getCustomerCalendar(day, customer);
    }

    private List<Calendar> getCustomerHolidayTime(CustomerEntity customer, Diapazon diapazon)
            throws ObjectNotFoundException {

        String workAtWeekEnd = propertyService.getValue(customer.getProperty(), "customer_workAtWeekEnd");

        IStringValidator validator = new BooleanYesValidator();
        if (validator.validate(workAtWeekEnd))
            return getCustomerWorkTime(customer, diapazon);

        List<Calendar> events = new ArrayList<>();
        events.add(new Calendar(
                diapazon.getDay(),
                diapazon.getStart().getTime(),
                diapazon.getStop().getTime()
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

    public List<Calendar> getTransportCalendar(Long day, TransportEntity transport) {

        return calendarRepository
                .findTransportCalendarByDay(transport.getId(), day)
                .stream()
                .map(entity ->
                        new Calendar(
                                entity.getId(),
                                entity.getDayNum(),
                                entity.getStartAt(),
                                entity.getStopAt()
                        )
                )
                .collect(Collectors.toList());
    }

    public List<Event> getCustomerCalendarWithOrders(String account, Long day)
            throws ObjectNotFoundException {

        CustomerEntity customer = customerService.getEntity(account);
        return getCustomerCalendarWithOrders(day, customer);
    }

    public List<Event> getCustomerCalendarWithOrders(Long day, CustomerEntity customer) {

        // достаём все календари
        List<Event> events = calendarRepository
                .findCustomerCalendarByDay(customer.getId(), day)
                .stream()
                .map(entity -> {
                    OrderEntity order = orderRepository.findByCustomerAndCalendar(customer, entity);
                    if (Objects.nonNull(order)) {
                        return new Event(orderMapper.toDto(order), calendarMapper.toDto(entity));
                    }
                    else {
                        return new Event(calendarMapper.toDto(entity), EventTypeEnum.UNAVAILABLE);
                    }
                })
        .collect(Collectors.toList());

        getCustomerWeekTime(day, customer)
                .stream()
                .forEach(calendar -> {
                    events.add(new Event(calendar, EventTypeEnum.GENERATED));
                });

        return events;
    }

    public List<Calendar> getCustomerCalendar(Long day, CustomerEntity customer) {

        List<Calendar> res = getCustomerWeekTime(day, customer);
        res.addAll(calendarRepository.findCustomerCalendarByDay(customer.getId(), day)
                .stream()
                .map(entity ->
                        new Calendar(
                                entity.getId(),
                                entity.getDayNum(),
                                entity.getStartAt(),
                                entity.getStopAt()
                        )
                )
                .collect(Collectors.toList())
        );

        return res;
    }

    private List<Calendar> getDriversCalendar(Long day, TransportEntity transport) {

        List<Calendar> result = new ArrayList();
        calendarRepository
                .findByDayNum(day)
                .stream()
                .forEach(entity -> {
                    Calendar calendar = new Calendar(
                            entity.getId(),
                            entity.getDayNum(),
                            entity.getStartAt(),
                            entity.getStopAt()
                    );

                    Boolean busy = true;

                    for(CustomerEntity customer: transport.getCustomer()) {
                        if (!getCustomerCalendar(day, customer).contains(calendar)) {
                            busy = false;
                            break;
                        }
                    }

                    if (busy)
                        result.add(calendar);
                });

        return result;
    }

    public List<Calendar> getTransportCalendar(String account, Long day, Long transportId) {

        List<Calendar> result = new ArrayList();

        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);

        List<Calendar> customerCalendar = calendarRepository
                .findCustomerCalendarByDay(customer.getId(), day)
                .stream()
                .map(entity ->
                        new Calendar(
                                entity.getId(),
                                entity.getDayNum(),
                                entity.getStartAt(),
                                entity.getStopAt()
                        )
                )
                .collect(Collectors.toList());

        List<Calendar> transportCalendar = getTransportCalendar(day, transport);
        List<Calendar> driverCalendar = getDriversCalendar(day, transport);

        calendarRepository
                .findByDayNum(day)
                .stream()
                .forEach(entity -> {
                    Calendar calendar = new Calendar(
                            entity.getId(),
                            entity.getDayNum(),
                            entity.getStartAt(),
                            entity.getStopAt()
                    );
                    if (customerCalendar.contains(calendar) || transportCalendar.contains(calendar) || driverCalendar.contains(calendar))
                        result.add(calendar);
                });

        return result;
    }

}

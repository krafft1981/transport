package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.dto.Event;
import com.rental.transport.dto.Order;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.RequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.mapper.CalendarMapper;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.mapper.RequestMapper;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.BooleanYesValidator;
import com.rental.transport.utils.validator.IStringValidator;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RequestMapper requestMapper;

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

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RequestRepository requestRepository;

    public Long getDayId(Long time) {

        java.util.Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(time);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    public CalendarEntity getEntity(Long day, Integer[] hours) {

        CalendarEntity entity = calendarRepository.findByDayAndHours(day, hours);
        if (Objects.isNull(entity)) {
            entity = new CalendarEntity(day, hours);
            calendarRepository.save(entity);
        }

        return entity;
    }

    public CalendarEntity getEntity(Long id) throws ObjectNotFoundException {

        return calendarRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Calendar", id));
    }

    private Map<Integer, Event> getCustomerWorkTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException, IllegalArgumentException {

        Map<Integer, Event> result = new HashMap();

        Integer startWorkAt = Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_startWorkTime"));
        Integer stopWorkAt = Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_stopWorkTime"));

        for (Integer hour = 0; hour < startWorkAt; hour++)
            result.put(hour, new Event(EventTypeEnum.GENERATED, day, 0, startWorkAt));
        for (Integer hour = startWorkAt; hour < stopWorkAt; hour++)
            result.put(hour, new Event(EventTypeEnum.FREE, day, startWorkAt, stopWorkAt));
        for (Integer hour = stopWorkAt; hour < 24; hour++)
            result.put(hour, new Event(EventTypeEnum.GENERATED, day, stopWorkAt, 24));

        return result;
    }

    private Map<Integer, Event> getCustomerHolidayTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException {

        String workAtWeekEnd = propertyService.getValue(customer.getProperty(), "customer_workAtWeekEnd");

        IStringValidator validator = new BooleanYesValidator();
        if (validator.validate(workAtWeekEnd))
            return getCustomerWorkTime(day, customer);

        Map<Integer, Event> result = new HashMap();
        for (Integer hour = 0; hour < 24; hour++)
            result.put(hour, new Event(EventTypeEnum.GENERATED, day, 0, 24));

        return result;
    }

    private Map<Integer, Event> getCustomerWeekTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException, IllegalArgumentException {

        Map<Integer, Event> result = new HashMap();
        java.util.Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(day);

        switch (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            case java.util.Calendar.MONDAY:
            case java.util.Calendar.TUESDAY:
            case java.util.Calendar.WEDNESDAY:
            case java.util.Calendar.THURSDAY:
            case java.util.Calendar.FRIDAY: {
                result.putAll(getCustomerWorkTime(getDayId(day), customer));
                break;
            }
            case java.util.Calendar.SATURDAY:
            case java.util.Calendar.SUNDAY: {
                result.putAll(getCustomerHolidayTime(getDayId(day), customer));
                break;
            }
        }

        return result;
    }

    public Map<Integer, Event> getTransportCalendar(Long day, TransportEntity transport) {

        Map<Integer, Event> result = new HashMap();
        calendarRepository
                .findCalendarByTransportIdAndDay(transport.getId(), getDayId(day))
                .stream()
                .forEach(entity -> {
                    Calendar calendar = calendarMapper.toDto(entity);
                    for (Integer hour : entity.getHours())
                        result.put(hour, new Event(calendar));
                });

        return result;
    }

    public Map<Integer, Event> getCustomerCalendar(Long day, CustomerEntity customer) {

        Map<Integer, Event> result = new HashMap();
        calendarRepository
                .findCalendarByCustomerIdAndDay(customer.getId(), getDayId(day))
                .stream()
                .forEach(entity -> {
                    Calendar calendar = calendarMapper.toDto(entity);
                    for (Integer hour : entity.getHours())
                        result.put(hour, new Event(calendar));
                });

        return result;
    }

    private Map<Integer, Event> getDriversCalendar(Long day, TransportEntity transport)
            throws ObjectNotFoundException {

        if (transport.getCustomer().isEmpty())
            throw new ObjectNotFoundException("Transport driver", transport.getId());

        CustomerEntity driver = transport.getCustomer().iterator().next();

        Map<Integer, Event> result = new HashMap();
        result.putAll(getCustomerWeekTime(day, driver));
        result.putAll(getCustomerCalendar(day, driver));

        return result;
    }

    public Map<Integer, Event> getTransportCalendar(CustomerEntity customer, TransportEntity transport, Long day) {

        Map<Integer, Event> result = new HashMap();
        Map<Integer, Event> transportCalendar = getTransportCalendar(day, transport);
        Map<Integer, Event> driverCalendar = getDriversCalendar(day, transport);
        Map<Integer, Event> customerCalendar = getCustomerCalendar(day, customer);

        for (Integer hour = 0; hour < 24; hour++) {
            if (customerCalendar.containsKey(hour)) {
                result.put(hour, customerCalendar.get(hour));
                continue;
            }

            if (transportCalendar.containsKey(hour)) {
                result.put(hour, transportCalendar.get(hour));
                continue;
            }

            if (driverCalendar.containsKey(hour)) {
                result.put(hour, driverCalendar.get(hour));
                continue;
            }
        }

        requestRepository
                .findByCustomerAndDay(transport.getId(), getDayId(day))
                .stream()
                .forEach(requestEntity -> {
                    switch (requestEntity.getStatus()) {
                        case NEW: {
                            for (Integer hour : requestEntity.getHours())
                                result.put(hour, new Event(EventTypeEnum.REQUEST, day, requestEntity.getHours()));
                            break;
                        }
                        case ACCEPTED: {
                            OrderEntity orderEntity = orderRepository.findById(requestEntity.getOrder()).orElse(null);
                            for (Integer hour : requestEntity.getHours())
                                result.put(hour, new Event(orderMapper.toDto(orderEntity)));
                            break;
                        }
                        case EXPIRED:
                        case REJECTED: {
                            for (Integer hour : requestEntity.getHours())
                                result.put(hour, new Event(EventTypeEnum.BUSY, requestMapper.toDto(requestEntity)));
                            break;
                        }
                    }
                });

        return result;
    }

    public Map<Integer, Event> getDriverCalendarWithOrders(Long day, CustomerEntity driver) {

        Map<Integer, Event> result = getCustomerWeekTime(day, driver);

        calendarRepository
                .findCalendarByCustomerIdAndDay(driver.getId(), day)
                .stream()
                .forEach(entity -> {
                    Calendar calendar = calendarMapper.toDto(entity);
                    for (Integer hour : entity.getHours())
                        result.put(hour, new Event(calendar));
                });

        orderRepository
                .findByDriverAndDay(driver, day)
                .stream()
                .forEach(entity -> {
                    Order order = orderMapper.toDto(entity);
                    for (Integer hour : entity.getHours())
                        result.put(hour, new Event(order));
                });

        return result;
    }

    public void checkCustomerBusy(CustomerEntity customer, Long day, Integer[] hours)
            throws IllegalArgumentException {

        Set<Integer> busyHours = new HashSet();
        calendarRepository
                .findCalendarByCustomerIdAndDay(customer.getId(), day)
                .stream()
                .forEach(entity -> busyHours.addAll(Arrays.asList(entity.getHours().clone())));

        for (Integer hour : hours) {
            if (busyHours.contains(hour))
                throw new IllegalArgumentException(String.format("Пользователь '%s' занят", customer.getId()));
        }
    }

    public void checkTransportBusy(TransportEntity transport, Long day, Integer[] hours)
            throws IllegalArgumentException {

        Set<Integer> busyHours = new HashSet();
        calendarRepository
                .findCalendarByTransportIdAndDay(transport.getId(), day)
                .stream()
                .forEach(entity -> busyHours.addAll(Arrays.asList(entity.getHours().clone())));

        for (Integer hour : hours) {
            if (busyHours.contains(hour))
                throw new IllegalArgumentException(String.format("Транспорт '%s' занят", transport.getId()));
        }
    }

    public void checkDriverBusy(TransportEntity transport, Long day, Integer[] hours)
            throws IllegalArgumentException {


    }
}

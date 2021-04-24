package com.rental.transport.service;

import com.rental.transport.dto.Event;
import com.rental.transport.dto.Order;
import com.rental.transport.dto.Request;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.RequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.mapper.RequestMapper;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import com.rental.transport.utils.validator.BooleanYesValidator;
import com.rental.transport.utils.validator.IStringValidator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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

    public Long getDayId(Long day) {

        java.util.Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(day);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);

        return calendar.getTime().getTime();
    }

    public Integer getHour(Date date) {

        Integer hour = date.getHours();
        return hour + 4;
    }

    public CalendarEntity getEntity(Long id) throws ObjectNotFoundException {

        return calendarRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Calendar", id));
    }

    public CalendarEntity getEntity(Long day, Integer hour, Boolean create)
            throws ObjectNotFoundException {

        day = getDayId(day);
        CalendarEntity entity = calendarRepository.findByDayNumAndHour(day, hour);
        if (Objects.isNull(entity)) {
            if (create) {
                entity = new CalendarEntity(day, hour);
                calendarRepository.save(entity);
            }
            else {
                String calendarName = String.format("day(%s) hour(%s)", day, hour);
                throw new ObjectNotFoundException("Calendar", calendarName);
            }
        }

        return entity;
    }

    private Map<Integer, Event> getCustomerWorkTime(CustomerEntity customer)
            throws ObjectNotFoundException, IllegalArgumentException {

        Map<Integer, Event> result = new HashMap();

        Integer startWorkAt = Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_startWorkTime"));
        Integer stopWorkAt = Integer.parseInt(propertyService.getValue(customer.getProperty(), "customer_stopWorkTime"));

        for (Integer hour = 0; hour < startWorkAt; hour++)
            result.put(hour, new Event(EventTypeEnum.GENERATED));

        for (Integer hour = startWorkAt; hour < stopWorkAt; hour++)
            result.put(hour, new Event(EventTypeEnum.FREE));

        for (Integer hour = stopWorkAt; hour < 24; hour++)
            result.put(hour, new Event(EventTypeEnum.GENERATED));

        return result;
    }

    private Map<Integer, Event> getCustomerHolidayTime(CustomerEntity customer)
            throws ObjectNotFoundException {

        String workAtWeekEnd = propertyService.getValue(customer.getProperty(), "customer_workAtWeekEnd");

        IStringValidator validator = new BooleanYesValidator();
        if (validator.validate(workAtWeekEnd))
            return getCustomerWorkTime(customer);

        Map<Integer, Event> result = new HashMap();

        for (Integer hour = 0; hour < 24; hour++)
            result.put(hour, new Event(EventTypeEnum.GENERATED));

        return result;
    }

    private Map<Integer, Event> getCustomerWeekTime(Long day, CustomerEntity customer)
            throws ObjectNotFoundException, IllegalArgumentException {

        day = getDayId(day);

        Map<Integer, Event> result = new HashMap();
        java.util.Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(day);

        switch (calendar.get(java.util.Calendar.DAY_OF_WEEK)) {
            case java.util.Calendar.MONDAY:
            case java.util.Calendar.TUESDAY:
            case java.util.Calendar.WEDNESDAY:
            case java.util.Calendar.THURSDAY:
            case java.util.Calendar.FRIDAY: {
                result.putAll(getCustomerWorkTime(customer));
                break;
            }
            case java.util.Calendar.SATURDAY:
            case java.util.Calendar.SUNDAY: {
                result.putAll(getCustomerHolidayTime(customer));
                break;
            }
        }

        return result;
    }

    public Map<Integer, Event> getTransportCalendar(Long day, TransportEntity transport) {

        day = getDayId(day);

        Map<Integer, Event> result = new HashMap();
        calendarRepository
                .findTransportCalendarByDay(transport.getId(), day)
                .stream()
                .forEach(entity -> result.put(entity.getHour(), new Event(EventTypeEnum.BUSY)));

        return result;
    }

    public Map<Integer, Event> getCustomerCalendar(Long day, CustomerEntity customer) {

        day = getDayId(day);

        Map<Integer, Event> result = new HashMap();
        calendarRepository
                .findCustomerCalendarByDay(customer.getId(), day)
                .stream()
                .forEach(entity -> result.put(entity.getHour(), new Event(EventTypeEnum.BUSY)));

        return result;
    }

    private Map<Integer, Event> getDriversCalendar(Long day, TransportEntity transport)
            throws ObjectNotFoundException {

        day = getDayId(day);

        if (transport.getCustomer().isEmpty())
            throw new ObjectNotFoundException("Transport driver", transport.getId());

        CustomerEntity driver = transport.getCustomer().iterator().next();

        Map<Integer, Event> result = getCustomerWeekTime(day, driver);
        result.putAll(getCustomerCalendar(day, driver));

        return result;
    }

    public Map<Integer, Event> getTransportCalendar(CustomerEntity customer, TransportEntity transport, Long day) {

        day = getDayId(day);

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
                .findByTransportAndDay(transport.getId(), day)
                .stream()
                .forEach(entity -> {
                    Request request = requestMapper.toDto(entity);
                    switch (entity.getStatus()) {
                        case NEW: {
                            result.put(entity.getCalendar().getHour(), new Event(request, EventTypeEnum.REQUEST));
                            break;
                        }
                        case ACCEPTED: {
                            OrderEntity orderEntity = orderRepository.findById(entity.getOrder()).orElse(null);
                            Order order = orderMapper.toDto(orderEntity);
                            result.put(entity.getCalendar().getHour(), new Event(order));
                            break;
                        }
                        case EXPIRED:
                        case REJECTED: {
                            result.put(entity.getCalendar().getHour(), new Event(request, EventTypeEnum.BUSY));
                            break;
                        }
                    }
                });

        return result;
    }

    public Map<Integer, Event> getCustomerCalendarWithOrders(Long day, CustomerEntity customer) {

        Map<Integer, Event> result = new HashMap();
        calendarRepository
                .findCustomerCalendarByDay(customer.getId(), day);
//                .stream()
//                .map(entity -> {
//                    OrderEntity order = orderRepository.findByCustomerAndCalendar(customer, entity);
//                    if (Objects.nonNull(order)) {
//                        return new Event(orderMapper.toDto(order), order.getCalendar()calendarMapper.toDto(entity));
//                    } else {
//                        return new Event(calendarMapper.toDto(entity), EventTypeEnum.UNAVAILABLE);
//                    }
//                })
//                .collect(Collectors.toList());
//
        result.putAll(getCustomerWeekTime(day, customer));

        return result;
    }
}

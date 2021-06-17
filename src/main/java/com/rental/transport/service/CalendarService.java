package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.dto.Event;
import com.rental.transport.dto.Text;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.CalendarTypeEnum;
import com.rental.transport.mapper.CalendarMapper;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TimeZone;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    @Autowired
    private CalendarMapper calendarMapper;

    @Autowired
    private TransportService transportService;

    @Autowired
    private WorkTimeService workTimeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Long getDayIdByTime(Long time) {

        java.util.Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(time);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar.getTime().getTime();
    }

    public CalendarEntity getEntity(Long id) throws ObjectNotFoundException {

        return calendarRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Calendar", id));
    }

    public CalendarEntity getEntity(Long day, Integer[] hours, CalendarTypeEnum type, Long objectId) {

        CalendarEntity entity = new CalendarEntity(day, hours, type, objectId);
        return calendarRepository.save(entity);
    }


    public Calendar createCalendarWithNote(String account, Long day, Integer[] hours, Text body)
            throws ObjectNotFoundException, IllegalArgumentException {

        day = getDayIdByTime(day);
        CustomerEntity customer = customerService.getEntity(account);
        checkCustomerBusy(customer, day, hours);
        if (Objects.isNull(body.getMessage()))
            throw new IllegalArgumentException("message не должно быть NULL");
        CalendarEntity calendar = new CalendarEntity(day, hours, CalendarTypeEnum.NOTE, customer.getId(), body.getMessage());
        calendarRepository.save(calendar);
        return calendarMapper.toDto(calendar);
    }

    public Calendar updateCalendarNote(String account, Long calendarId, Text body)
            throws ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        CalendarEntity calendar = getEntity(calendarId);
        if (Objects.isNull(body.getMessage()))
            throw new IllegalArgumentException("message не должно быть NULL");

        calendar.setNote(body.getMessage());
        calendarRepository.save(calendar);
        return calendarMapper.toDto(calendar);
    }

    public void deleteCalendarNote(String account, Long calendarId)
            throws IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        CalendarEntity calendar = getEntity(calendarId);
        if (calendar.getType() == CalendarTypeEnum.NOTE)
            calendarRepository.delete(calendar);
        else
            throw new IllegalArgumentException("Удалять можно только записи записной книги");
    }

    public void checkCustomerBusy(CustomerEntity customer, Long day, Integer[] hours)
            throws IllegalArgumentException {

        Set<Integer> busyHours = new HashSet();
        calendarRepository
                .findCalendarByDayAndTypeAndObjectId(day, CalendarTypeEnum.CUSTOMER, customer.getId())
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
                .findCalendarByDayAndTypeAndObjectId(day, CalendarTypeEnum.TRANSPORT, transport.getId())
                .stream()
                .forEach(entity -> busyHours.addAll(Arrays.asList(entity.getHours().clone())));

        for (Integer hour : hours) {
            if (busyHours.contains(hour))
                throw new IllegalArgumentException(String.format("Транспорт '%s' занят", transport.getId()));
        }
    }

    public List<Calendar> getTransportCalendar(Long day, TransportEntity transport) {

        return calendarRepository
                .findCalendarByDayAndTypeAndObjectId(day, CalendarTypeEnum.TRANSPORT, transport.getId())
                .stream()
                .map(entity -> calendarMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public List<Calendar> getCustomerCalendar(Long day, CustomerEntity customer) {

        return calendarRepository
                .findCalendarByDayAndTypeAndObjectId(day, CalendarTypeEnum.CUSTOMER, customer.getId())
                .stream()
                .map(entity -> calendarMapper.toDto(entity))
                .collect(Collectors.toList());
    }

    public Map<Integer, Event> getTransportEvents(String account, Long day, Long transportId) {

        Map<Integer, Event> result = new HashMap();
        day = getDayIdByTime(day);
        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);
        List<Calendar> customerCalendar = getCustomerCalendar(day, customer);

        Integer start = Integer.MAX_VALUE;
        Integer stop = Integer.MIN_VALUE;

        for (CustomerEntity driver : transport.getCustomer()) {
            Calendar calendar = workTimeService.getCustomerWeekTime(day, driver);
            if (start > calendar.getMinHour())
                start = calendar.getMinHour();
            if (stop < calendar.getMaxHour())
                stop = calendar.getMaxHour();
        }

        List<OrderEntity> orders = orderRepository.findByDriverAndDay(customer, day);
        return null;
    }

    public Map<Integer, Event> getCustomerEvents(String account, Long day) {
        day = getDayIdByTime(day);
        CustomerEntity customer = customerService.getEntity(account);


        return null;
    }
}


//    public Map<Integer, Event> getTransportEvents(Long day, Long transportId) {

//        TransportEntity transport = transportService.getEntity(transportId);
//        return getTransportCalendar(day, transport);
//    }

//    public Map<Integer, Event> getTransportCalendar(Long day, TransportEntity transport) {

//        Map<Integer, Event> result = new HashMap();
//        calendarRepository
//                .findCalendarByTransportIdAndDay(transport.getId(), getDayIdByTime(day))
//                .stream()
//                .forEach(entity -> {
//                    Calendar calendar = calendarMapper.toDto(entity);
//                    for (Integer hour : entity.getHours())
//                        result.put(hour, new Event(calendar));
//                });

//        return result;
//    }

//    public Map<Integer, Event> getCustomerCalendar(Long day, CustomerEntity customer) {

//        Map<Integer, Event> result = new HashMap();
//        calendarRepository
//                .findCalendarByCustomerIdAndDay(customer.getId(), getDayIdByTime(day))
//                .stream()
//                .forEach(entity -> {
//                    Calendar calendar = calendarMapper.toDto(entity);
//                    for (Integer hour : entity.getHours())
//                        result.put(hour, new Event(calendar));
//                });

//        return result;
//    }

//    private Map<Integer, Event> getDriversCalendar(Long day, TransportEntity transport)
//            throws ObjectNotFoundException {

//        if (transport.getCustomer().isEmpty())
//            throw new ObjectNotFoundException("Transport driver", transport.getId());
//
//        CustomerEntity driver = transport.getCustomer().iterator().next();

//        Map<Integer, Event> result = new HashMap();
//        result.putAll(workTimeService.getCustomerWeekTime(getDayIdByTime(day), driver));
//        result.putAll(getCustomerCalendar(day, driver));

//        return result;
//    }

//    public Map<Integer, Event> getTransportEvents(CustomerEntity customer, TransportEntity transport, Long day) {
//
//        return new HashMap();
//    }

//        Map<Integer, Event> result = new HashMap();
//        Map<Integer, Event> transportCalendar = getTransportCalendar(day, transport);
//        Map<Integer, Event> driverCalendar = getDriversCalendar(day, transport);
//        Map<Integer, Event> customerCalendar = getCustomerCalendar(day, customer);
//
//        for (Integer hour = 0; hour < 24; hour++) {
//            if (customerCalendar.containsKey(hour)) {
//                result.put(hour, customerCalendar.get(hour));
//                continue;
//            }
//
//            if (transportCalendar.containsKey(hour)) {
//                result.put(hour, transportCalendar.get(hour));
//                continue;
//            }
//
//            if (driverCalendar.containsKey(hour)) {
//                result.put(hour, driverCalendar.get(hour));
//                continue;
//            }
//        }
//
//        requestRepository
//                .findNewByCustomerAndDay(transport.getId(), getDayIdByTime(day))
//                .stream()
//                .forEach(requestEntity -> {
//                    switch (requestEntity.getStatus()) {
//                        case NEW: {
//                            for (Integer hour : requestEntity.getHours())
//                                result.put(hour, new Event(EventTypeEnum.REQUEST, day, requestEntity.getHours()));
//                            break;
//                        }
//                        case ACCEPTED: {
//                            OrderEntity orderEntity = orderRepository.findById(requestEntity.getOrder()).orElse(null);
//                            for (Integer hour : requestEntity.getHours())
//                                result.put(hour, new Event(orderMapper.toDto(orderEntity)));
//                            break;
//                        }
//                        case EXPIRED:
//                        case REJECTED: {
//                            for (Integer hour : requestEntity.getHours())
//                                result.put(hour, new Event(EventTypeEnum.BUSY, requestMapper.toDto(requestEntity)));
//                            break;
//                        }
//                    }
//                });
//
//        return result;
//    }
//
//    public Map<Integer, Event> getDriverCalendarWithOrders(Long day, CustomerEntity driver) {
//
//        Map<Integer, Event> result = workTimeService.getCustomerWeekTime(getDayIdByTime(day), driver);
//
//        calendarRepository
//                .findCalendarByCustomerIdAndDay(driver.getId(), day)
//                .stream()
//                .forEach(entity -> {
//                    Calendar calendar = calendarMapper.toDto(entity);
//                    for (Integer hour : entity.getHours())
//                        result.put(hour, new Event(calendar));
//                });
//
//        orderRepository
//                .findByDriverAndDay(driver, day)
//                .stream()
//                .forEach(entity -> {
//                    Order order = orderMapper.toDto(entity);
//                    for (Integer hour : entity.getHours())
//                        result.put(hour, new Event(order));
//                });
//
//        return result;
//    }
//
//    public Map<Integer, Event> getCustomerEvents(String account, Long day) throws ObjectNotFoundException {
//
//        CustomerEntity customer = customerService.getEntity(account);
//        return getDriverCalendarWithOrders(day, customer);
//        return new HashMap();
//    }
//}

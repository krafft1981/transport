package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.dto.Event;
import com.rental.transport.dto.Text;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderRepository;
import com.rental.transport.entity.RequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.enums.CalendarTypeEnum;
import com.rental.transport.enums.EventTypeEnum;
import com.rental.transport.mapper.CalendarMapper;
import com.rental.transport.mapper.OrderMapper;
import com.rental.transport.mapper.RequestMapper;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CalendarService {

    @Autowired
    private CalendarMapper calendarMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private TransportService transportService;

    @Autowired
    private WorkTimeService workTimeService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private CalendarRepository calendarRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RequestRepository requestRepository;

    public Long getDayIdByTime(Long time) {

        java.util.Calendar calendar = java.util.Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(time);
        calendar.set(java.util.Calendar.HOUR_OF_DAY, 0);
        calendar.set(java.util.Calendar.MINUTE, 0);
        calendar.set(java.util.Calendar.SECOND, 0);
        calendar.set(java.util.Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

    public CalendarEntity getEntity(Long id) throws ObjectNotFoundException {

        return calendarRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Calendar", id));
    }

    public CalendarEntity getEntity(Long day, Integer[] hours, CalendarTypeEnum type, Long objectId, Long orderId, String message) {

        CalendarEntity entity = new CalendarEntity(day, hours, type, objectId, orderId, message);
        return calendarRepository.save(entity);
    }

    public void checkObsolescence(Long day, CustomerEntity customer, Integer[] hours)
            throws IllegalArgumentException {

        java.util.Calendar calendar = java.util.Calendar.getInstance(TimeZone.getTimeZone(customer.getTimeZone()));
        calendar.setTime(new Date());

        final Long selectedDay = getDayIdByTime(calendar.getTimeInMillis());
        if (day > selectedDay)
            return;

        Integer currentHour = calendar.get(java.util.Calendar.HOUR_OF_DAY);
        if (day.equals(selectedDay)) {
            boolean last = false;
            for (Integer hour : hours) {
                if (hour <= currentHour) {
                    last = true;
                    break;
                }
            }

            if (!last)
                return;
        }

        throw new IllegalArgumentException("Сорян, время уже прошло");
    }

    public void sequenceCheck(Integer[] hours) throws IllegalArgumentException {

        Arrays.sort(hours);
        Integer current = null;
        for (Integer hour : hours) {
            if (current == null) {
                current = hour;
                continue;
            }

            if ((current + 1) != hour)
                throw new IllegalArgumentException("Выберите часы последовательно");

            current = hour;
        }
    }

    @Transactional
    public List<Event> createCalendarWithNote(String account, Long day, Integer[] hours, Text body)
            throws ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        final Long selectedDay = getDayIdByTime(day);

        checkObsolescence(selectedDay, customer, hours);
        sequenceCheck(hours);

        checkBusyByCustomer(customer, selectedDay, hours);
        checkBusyByNote(customer, selectedDay, hours);

        CalendarEntity calendar = new CalendarEntity(selectedDay, hours, CalendarTypeEnum.NOTE, customer.getId(), null, body.getMessage());
        calendarRepository.save(calendar);

        requestRepository
                .findNewByCustomerAndDay(customer.getId(), selectedDay)
                .forEach(entity -> {
                    for (Integer hour : hours) {
                        if (Arrays.asList(entity.getHours()).contains(hour)) {
                            requestService.rejectRequest(entity.getDriver().getAccount(), entity.getId());
                            break;
                        }
                    }
                });

        requestRepository
                .findNewByDriverAndDay(customer.getId(), selectedDay)
                .forEach(entity -> {
                    for (Integer hour : hours) {
                        if (Arrays.asList(entity.getHours()).contains(hour)) {
                            requestService.rejectRequest(entity.getDriver().getAccount(), entity.getId());
                            break;
                        }
                    }
                });

        return getCustomerEvents(account, selectedDay);
    }

    public List<Event> updateCalendarNote(String account, Long calendarId, Text body)
            throws ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        CalendarEntity calendar = getEntity(calendarId);
        calendar.setNote(body.getMessage());
        calendarRepository.save(calendar);
        return getCustomerEvents(account, calendar.getDay());
    }

    public List<Event> deleteCalendarNote(String account, Long calendarId)
            throws ObjectNotFoundException, IllegalArgumentException {

        CustomerEntity customer = customerService.getEntity(account);
        CalendarEntity calendar = getEntity(calendarId);
        if (calendar.getType() == CalendarTypeEnum.NOTE)
            calendarRepository.delete(calendar);
        else
            throw new IllegalArgumentException("Нельзя удалить заказ");

        return getCustomerEvents(account, calendar.getDay());
    }

    public void checkBusyByCustomer(CustomerEntity customer, Long day, Integer[] hours)
            throws IllegalArgumentException {

        Set<Integer> busyHours = new HashSet<>();
        calendarRepository
                .findByDayAndTypeAndObjectId(day, CalendarTypeEnum.CUSTOMER, customer.getId())
                .forEach(entity -> busyHours.addAll(Arrays.asList(entity.getHours().clone())));

        for (Integer hour : hours) {
            if (busyHours.contains(hour))
                throw new IllegalArgumentException("Пользователь занят");
        }
    }

    public void checkBusyByNote(CustomerEntity customer, Long day, Integer[] hours)
            throws IllegalArgumentException {

        Set<Integer> busyHours = new HashSet<>();
        calendarRepository
                .findByDayAndTypeAndObjectId(day, CalendarTypeEnum.NOTE, customer.getId())
                .forEach(entity -> busyHours.addAll(Arrays.asList(entity.getHours().clone())));

        for (Integer hour : hours) {
            if (busyHours.contains(hour))
                throw new IllegalArgumentException("Пользователь занят");
        }
    }

    //    Customer calendar (Показывается у водителя) +
    //    Берём за основу своё рабочее время. (Своё собственное) +
    //    Накладываем на него записи NOTE. +
    //    Накладываем на него записи занятости одобренные по заказам водителем. +
    public List<Event> getCustomerEvents(String account, Long day) {

        final Long selectedDay = getDayIdByTime(day);

        CustomerEntity customer = customerService.getEntity(account);
        List<Event> workTime = workTimeService.getCustomerWeekTime(selectedDay, customer);

        if (selectedDay < getDayIdByTime(new Date().getTime()))
            return workTime;

        calendarRepository
                .findByDayAndTypeAndObjectId(selectedDay, CalendarTypeEnum.NOTE, customer.getId())
                .forEach(entity -> workTime.add(new Event(EventTypeEnum.NOTE, calendarMapper.toDto(entity))));

        calendarRepository
                .findByDayAndTypeAndObjectId(selectedDay, CalendarTypeEnum.CUSTOMER, customer.getId())
                .forEach(entity -> workTime.add(new Event(EventTypeEnum.ORDER, calendarMapper.toDto(entity))));

        return workTime;
    }

    //    Transport calendar (Показывается у заказчика) +
    //    Берём за основу рабочее время транспорта (Первого водителя транспорта). +
    //    Накладываем на него записи NOTE. +
    //    Накладываем на него записи занятости одобренные по заказам водителем. +
    //    Накладываем на него собственную занятость. (заказы + заметки) +
    //    Накладываем жёлтым время созданных запросов +
    public List<Event> getTransportEvents(String account, Long day, Long transportId)
            throws ObjectNotFoundException, IllegalArgumentException {

        final Long selectedDay = getDayIdByTime(day);
        CustomerEntity customer = customerService.getEntity(account);
        TransportEntity transport = transportService.getEntity(transportId);
        if (transport.getCustomer().isEmpty())
            throw new IllegalArgumentException("Данный транспорт не имеет водителей");

        CustomerEntity driver = transport.getCustomer().iterator().next();

        List<Event> workTime = workTimeService.getCustomerWeekTime(selectedDay, driver);

        if (selectedDay < getDayIdByTime(new Date().getTime()))
            return workTime;

        calendarRepository
                .findByDayAndTypeAndObjectId(selectedDay, CalendarTypeEnum.NOTE, driver.getId())
                .forEach(entity -> {
                    Calendar calendar = calendarMapper.toDto(entity);
                    workTime.add(new Event(EventTypeEnum.BUSY, calendar));
                });

        calendarRepository
                .findByDayAndTypeAndObjectId(selectedDay, CalendarTypeEnum.CUSTOMER, driver.getId())
                .forEach(entity -> {
                    Calendar calendar = calendarMapper.toDto(entity);
                    workTime.add(new Event(EventTypeEnum.BUSY, calendar));
                });

        calendarRepository
                .findByDayAndTypeAndObjectId(selectedDay, CalendarTypeEnum.NOTE, customer.getId())
                .forEach(entity -> {
                    Calendar calendar = calendarMapper.toDto(entity);
                    workTime.add(new Event(EventTypeEnum.BUSY, calendar));
                });

        calendarRepository
                .findByDayAndTypeAndObjectId(selectedDay, CalendarTypeEnum.CUSTOMER, customer.getId())
                .forEach(entity -> {
                    Calendar calendar = calendarMapper.toDto(entity);
                    workTime.add(new Event(EventTypeEnum.BUSY, calendar));
                });

        orderRepository
                .findByCustomerAndTransportAndDay(customer, transport, selectedDay)
                .forEach(entity -> {
                    Calendar calendar = new Calendar(entity.getDay(), entity.getHours(), "");
                    workTime.add(new Event(EventTypeEnum.ORDER, calendar, entity.getId()));
                });

        requestRepository
                .findNewByCustomerAndTransportAndDay(customer.getId(), transport.getId(), selectedDay)
                .forEach(entity -> {
                    Calendar calendar = new Calendar(entity.getDay(), entity.getHours(), "");
                    workTime.add(new Event(EventTypeEnum.REQUEST, calendar, entity.getId()));
                });

        return workTime;
    }
}

package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.OrderRequestEntity;
import com.rental.transport.entity.OrderRequestRepository;
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
    private CalendarRepository calendarRepository;

    @Autowired
    private OrderRequestRepository orderRequestRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Long count() {

        Long count = calendarRepository.count();
        return count;
    }

    public void putOutEvent(String account, Date start, Date stop) throws IllegalArgumentException {

        CustomerEntity customer = customerRepository.findByAccount(account);
        CalendarEntity entity = new CalendarEntity();
        entity.setStartAt(start);
        entity.setStopAt(stop);
        entity.setCustomerId(customer);
        calendarRepository.save(entity);
    }

    public void deleteOutEvent(String account, Date start, Date stop) throws IllegalArgumentException {

        CustomerEntity customer = customerRepository.findByAccount(account);
        calendarRepository.deleteByCustomerIdAndStartAndStop(customer.getId(), start, stop);
    }

    public List<Calendar> getEventList(String account, Date start, Date stop) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        return getCalendarEvents(customer, start, stop);
    }

    public void сustomerOrderRequest(CustomerEntity customer, OrderEntity order, Date start, Date stop) {

        if (checkCustomerBusy(customer, start, stop) == false) {
            OrderRequestEntity orderRequest = new OrderRequestEntity(customer, order);
            orderRequestRepository.save(orderRequest);
        }
    }

    private Boolean checkTimeDiapazone(Integer diapazoneStart, Integer diapazoneStop, Integer start, Integer stop) {

        if ((start <= diapazoneStart) && (stop >= diapazoneStop))   return true;
        if ((start > diapazoneStart) && (stop < diapazoneStart))    return true;
        if ((start >= diapazoneStart) && (start < diapazoneStop))   return true;
        if ((stop >= diapazoneStart) && (stop < diapazoneStop))     return true;

        return false;
    }

    private Boolean checkCustomerBusy(CustomerEntity customer, Date start, Date stop) {

        for (Calendar event : getCalendarEvents(customer, start, stop)) {
            if (checkTimeDiapazone(
                    event.getStartAt(),
                    event.getStopAt(),
                    (int) start.getTime() / 1000,
                    (int) stop.getTime() / 1000))

                return true;
        }

        return false;
    }

    private List<Calendar> getCalendarEvents(CustomerEntity customer, Date start, Date stop) {

        List<Calendar> events = calendarRepository
                .findByCustomerIdUseStartAndStop(customer.getId(), start, stop)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());

/*
        if ((customer.getStartWorkAt() == customer.getStopWorkAt()) && customer.getWorkAtWeekEnd())

        while (true) {
            GregorianCalendar gc = new GregorianCalendar()
            gc.setTime(start);
            Integer day = gc.get(GregorianCalendar.DAY_OF_YEAR);

            break;
        }
*/
        return events;
    }

/*
        LocalDate current = start
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate finish = stop
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        while (true) {
            Calendar calendar = new Calendar(customer.getId());
            calendar.setCustomerId(customer.getId());
            switch (current.getDayOfWeek()) {
                case MONDAY:
                case TUESDAY:
                case WEDNESDAY:
                case THURSDAY:
                case FRIDAY: {
                    if ((customer.getStartWorkAt() != null) && (customer.getStopWorkAt() != null)) {
                        LocalDateTime stopWork = current.atTime(customer.getStopWorkAt(), 0);
                        LocalDateTime startWork = current.atTime(customer.getStartWorkAt(), 0).plusDays(1);
                        if (current.getDayOfWeek() == DayOfWeek.FRIDAY) {
                            if (customer.getWorkAtWeekEnd() == false) {
                                startWork = current.atTime(0, 0).plusDays(1);
                            }
                        }

                        System.out.println("1 Work Periodic: " + stopWork.toString() + " -> " + startWork.toString());

                        LocalDateTime now = stopWork;
                        Timestamp timestamp = Timestamp.valueOf(stopWork);
                        Long value = timestamp.getTime() / 1000;
                        calendar.setStartAt(value.intValue());
                        now = startWork;
                        value = timestamp.getTime() / 1000;
                        calendar.setStopAt(value.intValue());
                    }
                    break;
                }
                case SATURDAY:
                case SUNDAY: {
                    if (customer.getWorkAtWeekEnd() == false) {
                        if (current.getDayOfWeek() == DayOfWeek.SUNDAY) {

                            System.out.println("2 Work Periodic: " + current.atTime(0, 0).toString() + " -> " + current.atTime(customer.getStartWorkAt(), 0).plusDays(1).toString());

                            LocalDateTime now = current.atTime(0, 0);
                            Timestamp timestamp = Timestamp.valueOf(now);
                            Long value = timestamp.getTime() / 1000;
                            calendar.setStartAt(value.intValue());
                            now = current.atTime(customer.getStartWorkAt(), 0).plusDays(1);
                            value = timestamp.getTime() / 1000;
                            calendar.setStopAt(value.intValue());
                        }

                        else {

                            System.out.println("3 Work Periodic: " + current.atTime(0, 0).toString() + " -> " + current.atTime(0, 0).plusDays(1).toString());

                            LocalDateTime now = current.atTime(0, 0);
                            Timestamp timestamp = Timestamp.valueOf(now);
                            Long value = timestamp.getTime() / 1000;
                            calendar.setStartAt(value.intValue());
                            now = current.atTime(0, 0).plusDays(1);
                            value = timestamp.getTime() / 1000;
                            calendar.setStopAt(value.intValue());
                        }
                    }

                    else {
                        System.out.println("4 Work Periodic: " + current.atTime(customer.getStopWorkAt(), 0).toString() + " -> " + current.atTime(customer.getStartWorkAt(), 0).plusDays(1).toString());

                        LocalDateTime now = current.atTime(customer.getStopWorkAt(), 0);
                        Timestamp timestamp = Timestamp.valueOf(now);
                        Long value = timestamp.getTime() / 1000;
                        calendar.setStartAt(value.intValue());
                        now = current.atTime(customer.getStartWorkAt(), 0).plusDays(1);
                        value = timestamp.getTime() / 1000;
                        calendar.setStopAt(value.intValue());
                    }

                    break;
                }
            }

            busyList.add(calendar);
            current = current.plusDays(1);

            if (current.compareTo(finish) >= 0) {
                break;
            }
        }
*/
}

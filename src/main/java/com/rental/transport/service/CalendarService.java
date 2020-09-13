package com.rental.transport.service;

import com.rental.transport.dto.Calendar;
import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CalendarRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.CustomerRepository;
import com.rental.transport.mapper.CalendarMapper;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CalendarService {

    @Autowired
    private CalendarMapper mapper;

    @Autowired
    private CalendarRepository repository;

    @Autowired
    private CustomerRepository customerRepository;

    public Long count() {

        Long count = repository.count();
        return count;
    }

    public CustomerEntity validateByCustomersEvents(CustomerEntity customer, Date start, Date stop)
            throws IllegalArgumentException {

        if (Objects.nonNull(customer.getStartWorkAt()) && Objects.nonNull(customer.getStopWorkAt())) {

        }
//        throw new IllegalArgumentException("Временной диапазон занят в календаре");
        System.out.println("Customer: " + customer.getId() + " " + start.toString() + " -> " + stop.toString() + " " + customer.getStartWorkAt() + " -> " + customer.getStopWorkAt() + " " + customer.getWorkAtWeekEnd());
        return customer;
    }
/*
    public List<Calendar> getEvents(String account, Date start, Date stop) {

        CustomerEntity customer = customerRepository.findByAccount(account);

        List<Calendar> busyList = repository
                .getEntityByCustomerIdUseStartAndStop(customer.getId(), start, stop)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());

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
        return busyList;
    }
*/
    public void putOutEvent(String account, Date start, Date stop) throws IllegalArgumentException {

        CustomerEntity customer = customerRepository.findByAccount(account);
        CalendarEntity entity = new CalendarEntity();
        entity.setStartAt(start);
        entity.setStopAt(stop);
        entity.setCustomerId(customer);
        repository.save(entity);
    }

    public void deleteOutEvent(String account, Date start, Date stop) throws IllegalArgumentException {

        CustomerEntity customer = customerRepository.findByAccount(account);

        // ...
    }

    public List<Calendar> getEventList(String account, Date start, Date stop) {

        CustomerEntity customer = customerRepository.findByAccount(account);
        return repository
                .findByCustomerIdUseStartAndStop(customer.getId(), start, stop)
                .stream()
                .map(entity -> { return mapper.toDto(entity); })
                .collect(Collectors.toList());
    }
}

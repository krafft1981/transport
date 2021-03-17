package com.rental.transport.service;

import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.ConfirmationEntity;
import com.rental.transport.entity.ConfirmationRepository;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.utils.exceptions.IllegalArgumentException;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmationService {

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private ConfirmationRepository confirmationRepository;

    public List<OrderEntity> getByCustomer(CustomerEntity customer, Pageable pageable) {

        return confirmationRepository
                .getByCustomerId(customer.getId(), pageable)
                .stream()
                .map(entity -> entity.getOrder())
                .collect(Collectors.toList());
    }

    public List<OrderEntity> getByOrder(OrderEntity order, Pageable pageable) {

        return confirmationRepository
                .getByOrderId(order.getId(), pageable)
                .stream()
                .map(entity -> entity.getOrder())
                .collect(Collectors.toList());
    }

    public void deleteByOrderId(Long id) {

        confirmationRepository.deleteByOrderId(id);
    }

    public void interaction(CustomerEntity customer, OrderEntity order) {

        confirmationRepository.deleteByCustomerIdAndOrderId(customer, order);
    }

    @Transactional
    public void putOrder(OrderEntity order) throws IllegalArgumentException {

        AtomicLong result = new AtomicLong(0L);

        order
                .getTransport()
                .getCustomer()
                .stream()
                .forEach(customer -> {
                    CalendarEntity calendar = order.getCalendar().iterator().next();

                    try {
                        ConfirmationEntity entity = new ConfirmationEntity(customer, order);
                        calendarService.checkCustomerBusy(
                                customer,
                                calendar.getDayNum(),
                                calendar.getStartAt().getTime(),
                                calendar.getStopAt().getTime()
                        );
                        customer.addCalendar(calendar);
                        confirmationRepository.save(entity);
                        result.incrementAndGet();
                    }
                    catch (IllegalArgumentException e) {
                        customer.deleteCalendarEntity(calendar);
                    }
                });

        if (result.longValue() == 0)
            throw new java.lang.IllegalArgumentException("Transport has't free driver");
    }

    public ConfirmationEntity get(Long id) throws ObjectNotFoundException {

        return confirmationRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Confirmation", id));
    }
}

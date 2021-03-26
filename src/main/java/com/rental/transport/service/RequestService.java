package com.rental.transport.service;

import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.RequestEntity;
import com.rental.transport.entity.RequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public List<RequestEntity> getByCustomer(CustomerEntity customer, Pageable pageable) {

        return requestRepository.getByCustomerId(customer.getId(), pageable);
    }

    public List<RequestEntity> getByCustomer(CustomerEntity customer) {

        return requestRepository.getByCustomerId(customer.getId());
    }

    public List<RequestEntity> getByDriver(CustomerEntity driver, Pageable pageable) {

        return requestRepository.getByDriverId(driver.getId(), pageable);
    }

    public List<RequestEntity> getByDriver(CustomerEntity driver) {

        return requestRepository.getByDriverId(driver.getId());
    }

    public void deleteByCalendarId(Long calendarid) {

        requestRepository.deleteByCalendarId(calendarid);
    }

    public void putRequest(CustomerEntity customer, CustomerEntity driver, TransportEntity transport, CalendarEntity calendar) {

        RequestEntity entity = requestRepository.getByCustomerAndDriverAndTransportAndCalendar(customer, driver, transport, calendar);
        if (entity == null) {
            entity = new RequestEntity(customer, driver, transport, calendar);
            requestRepository.save(entity);
        }
    }

    public RequestEntity getEntity(Long id) throws ObjectNotFoundException {

        return requestRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Request", id));
    }

    public void setInteract(RequestEntity request) {

        requestRepository
                .getByCustomerAndTransportAndCalendar(request.getCustomer(), request.getTransport(), request.getCalendar())
                .stream()
                .forEach(entity -> entity.setInteract());
    }
}

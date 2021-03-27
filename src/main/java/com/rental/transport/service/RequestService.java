package com.rental.transport.service;

import com.rental.transport.entity.CalendarEntity;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.RequestEntity;
import com.rental.transport.entity.RequestRepository;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.utils.exceptions.ObjectNotFoundException;
import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RequestService {

    @Autowired
    private RequestRepository requestRepository;

    public List<RequestEntity> getByCustomer(CustomerEntity customer, Pageable pageable) {

        return requestRepository.findByCustomer(customer, pageable);
    }

    public List<RequestEntity> getByCustomer(CustomerEntity customer) {

        return requestRepository.findByCustomerAndInteractAtNull(customer);
    }

    public List<RequestEntity> getByDriver(CustomerEntity driver, Pageable pageable) {

        return requestRepository.findByDriver(driver, pageable);
    }

    public List<RequestEntity> getByTransport(TransportEntity transport) {

        return requestRepository.findByTransportAndInteractAtNull(transport);
    }

    public void putRequest(CustomerEntity customer, CustomerEntity driver, TransportEntity transport, CalendarEntity calendar) {

        if (requestRepository.findByCustomerAndTransportAndDriverAndCalendarAndInteractAtNull(customer, transport, driver, calendar).isEmpty()) {
            RequestEntity entity = new RequestEntity(customer, driver, transport, calendar);
            requestRepository.save(entity);
        }
    }

    public RequestEntity getEntity(Long id) throws ObjectNotFoundException {

        return requestRepository
                .findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Request", id));
    }

    public void setInteract(RequestEntity request, CustomerEntity driver, Long orderId) {

        requestRepository
                .findByCustomerAndTransportAndCalendarAndInteractAtNull(request.getCustomer(), request.getTransport(), request.getCalendar())
                .stream()
                .forEach(entity -> {
                    entity.setInteract();
                    if (Objects.nonNull(driver) && entity.getDriver().equals(driver))
                        entity.setOrder(orderId);
                });
    }
}

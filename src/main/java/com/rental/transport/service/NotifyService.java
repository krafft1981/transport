package com.rental.transport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rental.transport.dto.Event;
import com.rental.transport.dto.Notify;
import com.rental.transport.dto.Request;
import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.RequestEntity;
import com.rental.transport.entity.TransportEntity;
import com.rental.transport.mapper.CustomerMapper;
import com.rental.transport.mapper.RequestMapper;
import com.rental.transport.mapper.TransportMapper;
import java.io.StringWriter;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private EventService eventService;

    @Autowired
    private RequestService requestService;

    @Autowired
    private CalendarService calendarService;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TransportMapper transportMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    public void messageCreated(OrderEntity order) {

        eventService.sendMessage(order.getDriver(), "Для вас есть новое сообщение");
    }

    public void requestCreated(RequestEntity request) {

        List<Event> events = requestService.getRequestAsDriver(request.getDriver().getAccount());
        Notify notify = new Notify(requestMapper.toDto(request), events, "create");
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, notify);
            eventService.sendMessage(request.getDriver(), writer.toString());
        }
        catch (Exception e) {

        }
    }

    public void requestConfirmed(RequestEntity request) {

        List<Event> events = calendarService.getTransportEvents(
                request.getCustomer().getAccount(),
                request.getDay(),
                request.getTransport().getId()
        );
        Notify notify = new Notify(requestMapper.toDto(request), events, "confirm");
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, notify);
            eventService.sendMessage(request.getCustomer(), writer.toString());
        }
        catch (Exception e) {

        }
    }

    public void requestRejected(RequestEntity request) {

        List<Event> events = calendarService.getTransportEvents(
                request.getCustomer().getAccount(),
                request.getDay(),
                request.getTransport().getId()
        );
        Notify notify = new Notify(requestMapper.toDto(request), events, "reject");
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, notify);
            eventService.sendMessage(request.getCustomer(), writer.toString());
        }
        catch (Exception e) {

        }
    }

    public void requestCanceled(CustomerEntity driver, CustomerEntity customer, TransportEntity transport, Long day, Integer[] hours) {

        List<Event> events = calendarService.getTransportEvents(
                customer.getAccount(),
                day,
                transport.getId()
        );

        Request request = new Request(
                customerMapper.toDto(customer),
                customerMapper.toDto(driver),
                transportMapper.toDto(transport),
                day,
                hours
        );

        Notify notify = new Notify(request, events, "cancel");
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, notify);
            eventService.sendMessage(driver, writer.toString());
        }
        catch (Exception e) {

        }
    }
}

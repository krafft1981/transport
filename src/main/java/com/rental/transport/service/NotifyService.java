package com.rental.transport.service;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private EventService eventService;

    @Autowired
    private RequestMapper requestMapper;

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    private TransportMapper transportMapper;

    @Autowired
    private ObjectMapper objectMapper;

    public void messageCreated(OrderEntity order) {

        eventService.sendMessage(order.getDriver(), "Для вас есть новое сообщение");
    }

    public void requestCreated(RequestEntity request) {

        Notify notify = new Notify(requestMapper.toDto(request), "create");
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, notify);
            eventService.sendMessage(request.getDriver(), writer.toString());
        }
        catch (Exception e) {

        }
    }

    public void requestConfirmed(RequestEntity request) {

        Notify notify = new Notify(requestMapper.toDto(request), "confirm");
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, notify);
            eventService.sendMessage(request.getDriver(), writer.toString());
        }
        catch (Exception e) {

        }
    }

    public void requestRejected(RequestEntity request) {

        Notify notify = new Notify(requestMapper.toDto(request), "reject");
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, notify);
            eventService.sendMessage(request.getDriver(), writer.toString());
        }
        catch (Exception e) {

        }
    }

    public void requestCanceled(CustomerEntity driver, CustomerEntity customer, TransportEntity transport, Long day, Integer[] hours) {

        Request request = new Request(
                customerMapper.toDto(customer),
                customerMapper.toDto(driver),
                transportMapper.toDto(transport),
                day,
                hours
        );
        Notify notify = new Notify(request, "cancel");
        StringWriter writer = new StringWriter();
        try {
            objectMapper.writeValue(writer, notify);
            eventService.sendMessage(driver, writer.toString());
        }
        catch (Exception e) {

        }
    }
}

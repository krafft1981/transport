package com.rental.transport.service;

import com.rental.transport.entity.CustomerEntity;
import com.rental.transport.entity.OrderEntity;
import com.rental.transport.entity.RequestEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotifyService {

    @Autowired
    private PropertyService propertyService;

    @Autowired
    private EventHandlerService eventHandlerService;

    public void messageCreated(OrderEntity order) {

        eventHandlerService.sendMessage(order.getDriver(), "Для вас есть новое сообщение");
    }

    public void requestCreated(RequestEntity request) {

        String name = propertyService.getValue(request.getDriver().getProperty(), "customer_fio");
        eventHandlerService.sendMessage(request.getDriver(), name + " создал новый заказ");
    }

    public void requestConfirmed(RequestEntity request) {

        String name = propertyService.getValue(request.getCustomer().getProperty(), "customer_fio");
        eventHandlerService.sendMessage(request.getCustomer(), name + ", Ваш заказ принят");
    }

    public void requestRejected(RequestEntity request) {

        eventHandlerService.sendMessage(request.getCustomer(), "Извините. Заказ не может быть выполнен");
    }

    public void requestCanceled(CustomerEntity customer, Long day) {

        String name = propertyService.getValue(customer.getProperty(), "customer_fio");
        eventHandlerService.sendMessage(customer, name + " отменил заказ");
    }
}

package com.rental.transport.confirmation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
@Service
public class EventPublisher {
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    public void publishOrderEvent(final String message) {
        System.out.println("Publishing custom event. ");
        Event event = new Event(this, message);
        applicationEventPublisher.publishEvent(event);
    }
}
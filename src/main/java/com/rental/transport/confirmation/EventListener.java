package com.rental.transport.confirmation;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class EventListener implements ApplicationListener<Event> {
    @Override
    public void onApplicationEvent(Event event) {
        System.out.println("Received spring custom event - " + event.getMessage());
    }
}
package com.rental.transport.confirmation;

import org.springframework.context.ApplicationEvent;

public class Event extends ApplicationEvent {
    private String message;

    public Event(Object source, String message) {
        super(source);
        this.message = message;
    }
    public String getMessage() {
        return message;
    }
}

//https://www.baeldubg.com/spring-events

package com.rental.transport.utils.exceptions;

public class TransportNotFoundException extends RuntimeException {

    public TransportNotFoundException(Long id) {

        super(String.format("Transport with Id %d not found", id));
    }
}

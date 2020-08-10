package com.rental.transport.utils.exceptions;

public class DriverNotFoundException extends RuntimeException {

    public DriverNotFoundException(Long id) {

        super(String.format("Author with Id %d not found", id));
    }
}

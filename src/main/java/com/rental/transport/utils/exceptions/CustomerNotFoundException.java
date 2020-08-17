package com.rental.transport.utils.exceptions;

public class CustomerNotFoundException extends RuntimeException {

    public CustomerNotFoundException(String account) {

        super(String.format("Customer '%s' not found", account));
    }

    public CustomerNotFoundException(Long id) {

        super(String.format("Customer %d not found", id));
    }
}

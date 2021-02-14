package com.rental.transport.utils.exceptions;

public class AccessDeniedException extends RuntimeException {

    public AccessDeniedException(String action) {

        super(String.format("%s not your object is denied", action));
    }
}

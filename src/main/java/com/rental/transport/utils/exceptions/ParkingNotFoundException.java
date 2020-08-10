package com.rental.transport.utils.exceptions;

public class ParkingNotFoundException extends RuntimeException {

    public ParkingNotFoundException(Long id) {

        super(String.format("Book with Id %d not found", id));
    }
}

package com.rental.transport.utils.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String type, Long id) {

        super(String.format("%s with Id(%d) not found", type, id));
    }

    public ObjectNotFoundException(String type, String name) {

        super(String.format("%s with name(%s) not found", type, name));
    }

    public ObjectNotFoundException(Long id) {

        super(String.format("Object with id(%d) not found", id));
    }
}

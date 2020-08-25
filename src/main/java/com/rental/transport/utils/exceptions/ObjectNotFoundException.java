package com.rental.transport.utils.exceptions;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String type, Long id) {

        super(String.format("%s с Id(%d) не найден", type, id));
    }

    public ObjectNotFoundException(String type, String name) {

        super(String.format("%s с именем(%s) не найден", type, name));
    }
}

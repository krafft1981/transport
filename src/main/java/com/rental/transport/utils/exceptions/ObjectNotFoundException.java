package com.rental.transport.utils.exceptions;

import java.util.UUID;

public class ObjectNotFoundException extends RuntimeException {

    public ObjectNotFoundException(String type, UUID id) {

        super(String.format("%s с id(%d) не найден", type, id));
    }

    public ObjectNotFoundException(String type, String name) {

        super(String.format("%s с name(%s) не найден", type, name));
    }

    public ObjectNotFoundException(Long id) {

        super(String.format("Обьект с id(%d) не найден", id));
    }
}

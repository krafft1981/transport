package com.rental.transport.enums;

import lombok.Getter;

@Getter
public enum RequestStatusEnum {

    NEW(1, "NEW"),
    ACCEPTED(2, "ACCEPTED"),
    REJECTED(3, "REJECTED"),
    EXPIRED(4, "EXPIRED");

    private int id;
    private String name;

    RequestStatusEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

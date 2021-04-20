package com.rental.transport.enums;

import lombok.Getter;

public enum RequestStatusEnum {

    NEW(1, "New"),
    ACCEPTED(2, "Accepted"),
    REJECTED(3, "Rejected"),
    EXPIRED(4, "Expired");

    @Getter
    private int id = 0;
    @Getter
    private String name;

    RequestStatusEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

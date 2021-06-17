package com.rental.transport.enums;

import lombok.Getter;

public enum CalendarTypeEnum {

    CUSTOMER(1, "CUSTOMER"),
    TRANSPORT(2, "TRANSPORT"),
    NOTE(3, "NOTE");

    @Getter
    private int id = 0;
    @Getter
    private String name;

    CalendarTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

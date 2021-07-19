package com.rental.transport.enums;

import lombok.Getter;

@Getter
public enum CalendarTypeEnum {

    CUSTOMER(1, "CUSTOMER"),
    NOTE(2, "NOTE");

    private int id = 0;
    private String name;

    CalendarTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

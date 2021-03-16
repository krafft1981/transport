package com.rental.transport.enums;

import lombok.Getter;

public enum CalendarEventTypeEnum {

    GENERATED(1L, "Generated"),
    UNAVAILABLE(2L, "Unavailable"),
    ORDER(3L, "Order");

    @Getter
    private Long id;
    @Getter
    private String name;

    CalendarEventTypeEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

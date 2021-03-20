package com.rental.transport.enums;

import lombok.Getter;

public enum EventTypeEnum {

    GENERATED(1L, "Generated"),
    UNAVAILABLE(2L, "Unavailable"),
    ORDER(3L, "Order");

    @Getter
    private Long id;
    @Getter
    private String name;

    EventTypeEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}

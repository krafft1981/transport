package com.rental.transport.enums;

import lombok.Getter;

public enum EventTypeEnum {

    UNCKNOWN(0, "Uncknown"),
    GENERATED(1, "Generated"),
    NOTE(2, "Note"),
    REQUEST(3, "Request"),
    ORDER(4, "Order"),
    BUSY(5, "Busy"),
    FREE(6, "Free");

    @Getter
    private int id = 0;
    @Getter
    private String name;

    public int id() {
        return id;
    }

    EventTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

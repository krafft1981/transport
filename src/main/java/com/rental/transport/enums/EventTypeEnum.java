package com.rental.transport.enums;

import lombok.Getter;

@Getter
public enum EventTypeEnum {

    UNCKNOWN(0, "Uncknown"),
    GENERATED(1, "Generated"),
    NOTE(2, "Note"),
    REQUEST(3, "Request"),
    ORDER(4, "Order"),
    BUSY(5, "Busy"),
    FREE(6, "Free"),
    EXPIRED(7, "Expired");

    private int id;
    private String name;

    EventTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

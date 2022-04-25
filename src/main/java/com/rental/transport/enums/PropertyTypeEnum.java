package com.rental.transport.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyTypeEnum {

    STRING("String"),
    PHONE("Phone"),
    INTEGER("Integer"),
    HOUR("Hour"),
    DOUBLE("Double"),
    BOOLEAN("Boolean"),
    EMAIL("Email"),
    PASSWORD("Password");

    private String type;
}

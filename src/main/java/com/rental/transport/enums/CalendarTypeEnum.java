package com.rental.transport.enums;

import lombok.Getter;

@Getter
public enum CalendarTypeEnum {

    ORDER(1, "ORDER"),          // запись пришедшая из заказа
    NOTE(2, "NOTE"),            // запись записной книги
    REQUEST(3, "REQUEST");      // запись пришедшая из запроса

    private int id = 0;
    private String name;

    CalendarTypeEnum(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

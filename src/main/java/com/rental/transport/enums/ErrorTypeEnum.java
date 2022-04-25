package com.rental.transport.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorTypeEnum {

    TIME_IS_OUT("Сорян, время уже прошло"),
    SELECT_CLOCK_SEQUENCE("Выберите часы последовательно"),
    REMOVE_RECORD_DENY("Нельзя удалить запись"),
    CUSTOMER_BUSY("Пользователь занят"),
    CUSTOMER_ALREADY_EXIST("Учётная запись уже существует"),
    TRANSPORT_NO_DRIVER("Данный транспорт не имеет водителей");

    private String name;
}

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
    TRANSPORT_NO_DRIVER("Данный транспорт не имеет водителей"),
    CUSTOMER_EMAIL_WRONG_FIELD_MAIL("Неправильное значение поля 'почта'"),
    CUSTOMER_EMAIL_WRONG_FIELD_PHONE("Неправильное значение поля 'телефон'"),
    CUSTOMER_EMAIL_WRONG_FIELD_NAME("Неправильное значение поля 'имя'"),
    CUSTOMER_EMAIL_WRONG_FIELD_PASSWORD_LENGTH("Пароль должен быть не короче 4х символов");

    private final String name;
}

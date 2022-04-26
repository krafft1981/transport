package com.rental.transport.enums;

public enum RequestStatusEnum {

    NEW,              // Созданный
    ACCEPTED_NOPAY,   // Подтверждённый капитаном но не оплаченный
    ACCEPTED,         // Подтверждённый капитаном и оплаченный
    REJECTED,         // Отвергнутые
    EXPIRED,          // Заявка в виду не активности устарела
}

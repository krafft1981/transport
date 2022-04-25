package com.rental.transport.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PropertyNameEnum {

    CUSTOMER_START_WORK_TIME("customer_start_work_time", "Час начала работы", PropertyTypeEnum.HOUR),
    CUSTOMER_STOP_WORK_TIME("customer_stop_work_time", "Час окончания работы", PropertyTypeEnum.HOUR),
    CUSTOMER_NAME("customer_name", "Имя", PropertyTypeEnum.STRING),
    CUSTOMER_PHONE("customer_phone", "Сотовый", PropertyTypeEnum.PHONE),
    CUSTOMER_WORK_AT_WEEK_END("customer_work_at_week_end", "Работает в выходные", PropertyTypeEnum.BOOLEAN),
    CUSTOMER_DESCRIPTION("customer_description", "Описание", PropertyTypeEnum.STRING),
    CUSTOMER_REQUEST_DURATION("customer_request_duration", "Продолжительность запроса(минут)", PropertyTypeEnum.INTEGER),
    CUSTOMER_CARD_NUMBER("customer_card_number", "Номер карты", PropertyTypeEnum.STRING),

    TRANSPORT_NAME("transport_name", "Название", PropertyTypeEnum.STRING),
    TRANSPORT_CAPACITY("transport_capacity", "Максимальное количество гостей", PropertyTypeEnum.INTEGER),
    TRANSPORT_PRICE("transport_price", "Цена за час", PropertyTypeEnum.DOUBLE),
    TRANSPORT_MIN_RENT("transport_min_rent_time", "Минимальное время аренды(часов)", PropertyTypeEnum.HOUR),
    TRANSPORT_DESCRIPTION("transport_description", "Описание", PropertyTypeEnum.STRING),

    PARKING_NAME("parking_name", "Название", PropertyTypeEnum.STRING),
    PARKING_LATITUDE("parking_latitude", "Широта", PropertyTypeEnum.DOUBLE),
    PARKING_LONGITUDE("parking_longitude", "Долгота", PropertyTypeEnum.DOUBLE),
    PARKING_ADDRESS("parking_address", "Адрес", PropertyTypeEnum.STRING),
    PARKING_LOCALITY("parking_locality", "Местонахождение", PropertyTypeEnum.STRING),
    PARKING_REGION("parking_region", "Район", PropertyTypeEnum.STRING),
    PARKING_DESCRIPTION("parking_description", "Описание", PropertyTypeEnum.STRING),

    ORDER_PARKING_NAME("order_parking_name", "Название стоянки", PropertyTypeEnum.STRING),
    ORDER_PARKING_LATITUDE("order_parking_latitude", "Широта", PropertyTypeEnum.DOUBLE),
    ORDER_PARKING_LONGITUDE("order_parking_longitude", "Долгота", PropertyTypeEnum.DOUBLE),
    ORDER_PARKING_ADDRESS("order_parking_address", "Адрес стоянки", PropertyTypeEnum.STRING),
    ORDER_PARKING_LOCALITY("order_parking_locality", "Местонахождение", PropertyTypeEnum.STRING),
    ORDER_PARKING_REGION("order_parking_region", "Район", PropertyTypeEnum.STRING),
    ORDER_TRANSPORT_TYPE("order_transport_type", "Тип транспорта", PropertyTypeEnum.STRING),
    ORDER_TRANSPORT_NAME("order_transport_name", "Название транспорта", PropertyTypeEnum.STRING),
    ORDER_TRANSPORT_CAPACITY("order_transport_capacity", "Количество гостей", PropertyTypeEnum.INTEGER),
    ORDER_TRANSPORT_COST("order_transport_cost", "Стоимость заказа", PropertyTypeEnum.DOUBLE),
    ORDER_TRANSPORT_PRICE("order_transport_price", "Стоимость за час", PropertyTypeEnum.DOUBLE),
    ORDER_CUSTOMER_NAME("order_customer_fio", "Имя заказчика", PropertyTypeEnum.STRING),
    ORDER_CUSTOMER_PHONE("order_customer_phone", "Сотовый заказчика", PropertyTypeEnum.PHONE),
    ORDER_DRIVER_NAME("order_driver_fio", "Имя капитана", PropertyTypeEnum.STRING),
    ORDER_DRIVER_PHONE("order_driver_phone", "Сотовый капитана", PropertyTypeEnum.PHONE),
    ORDER_TIME_DAY("order_time_day", "День заказа", PropertyTypeEnum.INTEGER),
    ORDER_TIME_HOURS("order_time_hours", "Часы заказа", PropertyTypeEnum.STRING),
    ORDER_TIME_DURATION("order_time_duration", "Продолжительность заказа", PropertyTypeEnum.INTEGER);

    private String logicName;
    private String humanName;
    private PropertyTypeEnum type;
}

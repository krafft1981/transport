package com.rental.transport.validator;

import com.rental.transport.enums.PropertyTypeEnum;

public final class ValidatorFactory {

    public IStringValidator getValidator(final String type) throws IllegalArgumentException {

        return switch (PropertyTypeEnum.valueOf(type)) {
            case STRING -> new StringValidator();
            case PHONE -> new PhoneValidator();
            case INTEGER -> new IntegerValidator();
            case HOUR -> new HourValidator();
            case DOUBLE -> new DoubleValidator();
            case BOOLEAN -> new BooleanValidator();
            case EMAIL -> new EmailValidator();
            case PASSWORD -> new PasswordValidator();
            case CARDNUMBER -> new CardNumberValidator();
        };
    }
}

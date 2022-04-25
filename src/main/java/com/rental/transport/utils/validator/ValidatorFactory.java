package com.rental.transport.utils.validator;

import com.rental.transport.enums.PropertyTypeEnum;
import org.springframework.stereotype.Component;

@Component
public final class ValidatorFactory {

    public IStringValidator getValidator(PropertyTypeEnum type) {

        switch (type) {
            case PHONE:
                return new PhoneValidator();
            case INTEGER:
                return new IntegerValidator();
            case HOUR:
                return new HourValidator();
            case DOUBLE:
                return new DoubleValidator();
            case BOOLEAN:
                return new BooleanValidator();
            case EMAIL:
                return new EmailValidator();
            case PASSWORD:
                return new PasswordValidator();
            default:
                return new StringValidator();
        }
    }
}

package com.rental.transport.validator;

import java.util.regex.Pattern;

public class PhoneValidator implements IStringValidator {

    StringValidator validator = new StringValidator();
    private Pattern pattern = Pattern.compile("\\d{11}");

    @Override
    public Boolean validate(String value) {

        if (!validator.validate(value)) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}

package com.rental.transport.validator;

import java.util.regex.Pattern;

public class HourValidator implements IStringValidator {

    StringValidator validator = new StringValidator();
    private Pattern pattern = Pattern.compile("([01]?[0-9]|2[0-4])");

    @Override
    public Boolean validate(String value) {

        if (!validator.validate(value))
            return false;

        return pattern.matcher(value).matches();
    }
}

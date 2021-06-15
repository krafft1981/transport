package com.rental.transport.utils.validator;

import java.util.regex.Pattern;

public class DoubleValidator implements IStringValidator {

    StringValidator validator = new StringValidator();
    private Pattern pattern = Pattern.compile("[0-9]+.[0-9]+|[0-9]+");

    @Override
    public Boolean validate(String value) {

        if (!validator.validate(value))
            return false;

        return pattern.matcher(value).matches();
    }
}

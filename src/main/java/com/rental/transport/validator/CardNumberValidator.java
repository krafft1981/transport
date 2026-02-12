package com.rental.transport.validator;

import java.util.regex.Pattern;

public class CardNumberValidator implements IStringValidator {

    StringValidator validator = new StringValidator();
    private Pattern pattern = Pattern.compile("(?<=^|[^0-9])[0-9]{16}(?=[^0-9]|$)|[0-9]{4}[-| |_][0-9]{4}[-| |_][0-9]{4}[-| |_][0-9]{4}");

    @Override
    public Boolean validate(String value) {

        if (!validator.validate(value))
            return false;

        return pattern.matcher(value).matches();
    }
}

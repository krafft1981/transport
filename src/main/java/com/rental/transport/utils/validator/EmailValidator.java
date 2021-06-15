package com.rental.transport.utils.validator;

import java.util.regex.Pattern;

public class EmailValidator implements IStringValidator {

    StringValidator validator = new StringValidator();
    private Pattern pattern = Pattern.compile("\\b[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}\\b");

    @Override
    public Boolean validate(String value) {

        if (!validator.validate(value))
            return false;

        return pattern.matcher(value).matches();
    }
}

package com.rental.transport.validator;

import java.util.regex.Pattern;

public class BooleanValidator implements IStringValidator {

    StringValidator validator = new StringValidator();
    private Pattern pattern = Pattern.compile("[Нн][Ее][Тт]|[Дд][Аа]|[Tt][Rr][Uu][Ee]|[Ff][Aa][Ll][Ss][Ee]|0|1|[Yy][Ee][Ss]|[Nn][Oo]");

    @Override
    public Boolean validate(String value) {

        if (!validator.validate(value)) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}

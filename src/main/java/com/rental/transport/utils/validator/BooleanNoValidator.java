package com.rental.transport.utils.validator;

import java.util.regex.Pattern;

public class BooleanNoValidator implements IStringValidator {

    StringValidator validator = new StringValidator();
    private Pattern pattern = Pattern.compile("[Нн][Ее][Тт]|[Ff][aA][Ll][Ss][Ee]|0|[Nn][Oo]");

    @Override
    public Boolean validate(String value) {

        if (!validator.validate(value))
            return false;

        return pattern.matcher(value).matches();
    }
}

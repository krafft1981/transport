package com.rental.transport.utils.validator;

import java.util.regex.Pattern;

public class BooleanYesValidator implements IStringValidator {

    StringValidator validator = new StringValidator();
    private Pattern pattern = Pattern.compile("[Дд][Аа]|[Tt][Rr][Uu][Ee]|1|[Yy][Ee][Ss]");

    @Override
    public Boolean validate(String value) {

        if (!validator.validate(value)) {
            return false;
        }

        return pattern.matcher(value).matches();
    }
}

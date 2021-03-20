package com.rental.transport.utils.validator;

public class StringValidator implements IStringValidator {

    @Override
    public Boolean validate(String value) {

        return(value != null && !value.trim().isEmpty());
    }
}

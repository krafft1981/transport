package com.rental.transport.validator;

public class BooleanValidator implements IStringValidator {

    final BooleanYesValidator booleanYesValidator = new BooleanYesValidator();
    final BooleanNoValidator booleanNoValidator = new BooleanNoValidator();

    @Override
    public Boolean validate(String value) {

        return booleanYesValidator.validate(value) || booleanNoValidator.validate(value);
    }
}

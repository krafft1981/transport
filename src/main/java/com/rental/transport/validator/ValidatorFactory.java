package com.rental.transport.validator;

public final class ValidatorFactory {

    public IStringValidator getValidator(String type) throws IllegalArgumentException {

        if (type.equals("String"))	return new StringValidator();
        if (type.equals("Phone"))	return new PhoneValidator();
        if (type.equals("Integer"))	return new IntegerValidator();
        if (type.equals("Hour"))	return new HourValidator();
        if (type.equals("Double"))	return new DoubleValidator();
        if (type.equals("Boolean"))	return new BooleanValidator();
        if (type.equals("Email"))	return new EmailValidator();
        if (type.equals("Password"))	return new PasswordValidator();

        throw new IllegalArgumentException("Uncknown type: " + type);
    }
}

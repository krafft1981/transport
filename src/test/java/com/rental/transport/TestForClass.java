package com.rental.transport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Аннотация для ассоциации тестируемого класса с классом unit-тестов.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TestForClass {

    /**
     * @return Возвращает тип тестируемого класса.
     */
    Class<?> value();
}

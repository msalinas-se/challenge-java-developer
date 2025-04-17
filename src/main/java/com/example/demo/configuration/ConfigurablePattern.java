package com.example.demo.configuration;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Anotación de validación que permite configurar la expresión regular desde application.properties.
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ConfigurablePatternValidator.class)
@Documented
public @interface ConfigurablePattern {

    String message() default "Formato inválido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String property();
}

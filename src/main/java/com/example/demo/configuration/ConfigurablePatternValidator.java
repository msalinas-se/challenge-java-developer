package com.example.demo.configuration;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Validador que lee la expresión regular configurada en application.properties.
 */
@Component
public class ConfigurablePatternValidator implements ConstraintValidator<ConfigurablePattern, String> {

    private final Environment environment;
    private Pattern pattern;
    private String message;

    public ConfigurablePatternValidator(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void initialize(ConfigurablePattern annotation) {
        this.message = annotation.message();
        String regex = environment.getProperty(annotation.property());
        if (regex == null) {
            throw new IllegalStateException(
                    "No se encontró la propiedad de regex: " + annotation.property());
        }
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || pattern.matcher(value).matches()) {
            return true;
        }
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
        return false;
    }
}

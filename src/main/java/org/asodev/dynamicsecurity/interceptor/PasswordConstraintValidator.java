package org.asodev.dynamicsecurity.interceptor;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
        /* no-op */ }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            errors.add("Parola boş olamaz");
        } else {
            int len = password.length();
            if (len < 8 || len > 100) {
                errors.add("Parola uzunluğu 8-100 karakter arasında olmalıdır");
            }
            if (!password.chars().anyMatch(Character::isUpperCase)) {
                errors.add("En az bir büyük harf olmalıdır");
            }
            if (!password.chars().anyMatch(Character::isLowerCase)) {
                errors.add("En az bir küçük harf olmalıdır");
            }
            if (!password.chars().anyMatch(Character::isDigit)) {
                errors.add("En az bir rakam olmalıdır");
            }
            if (password.chars().allMatch(Character::isLetterOrDigit)) {
                errors.add("En az bir özel karakter olmalıdır");
            }
        }

        if (errors.isEmpty()) {
            return true;
        }

        // Disable default message and add each violation separately
        context.disableDefaultConstraintViolation();
        for (String err : errors) {
            context.buildConstraintViolationWithTemplate(err)
                    .addConstraintViolation();
        }
        return false;
    }
}

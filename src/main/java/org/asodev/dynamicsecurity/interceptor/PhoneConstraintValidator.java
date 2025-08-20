package org.asodev.dynamicsecurity.interceptor;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneConstraintValidator implements ConstraintValidator<ValidPhone, String> {

    // E.164 (recommended) or local numbers (digits, spaces, parentheses, dashes)
    private static final String E164_REGEX = "^\\+?[1-9]\\d{1,14}$";
    private static final String FLEXIBLE_REGEX = "^[0-9()+\\-\\s]{6,30}$";

    @Override
    public void initialize(ValidPhone constraintAnnotation) {
        /* no-op */ }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        if (phone == null || phone.isBlank()) {
            return true;
        }
        String trimmed = phone.trim();

        return trimmed.matches(E164_REGEX) || trimmed.matches(FLEXIBLE_REGEX);
    }
}

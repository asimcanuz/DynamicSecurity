package org.asodev.dynamicsecurity.interceptor;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = PhoneConstraintValidator.class)
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidPhone {

    String message() default "Invalid phone number (must be E.164 or local digits)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

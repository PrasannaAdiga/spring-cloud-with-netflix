package com.learning.cloud.custom.annotation;

import com.learning.cloud.custom.validator.UUIDValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target({ElementType.FIELD, ElementType.PARAMETER})
@Constraint(validatedBy={UUIDValidator.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUUID {
    String message() default "Invalid uuid format";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

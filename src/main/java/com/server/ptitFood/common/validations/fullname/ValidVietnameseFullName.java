package com.server.ptitFood.common.validations.fullname;

import com.server.ptitFood.common.validations.password.PasswordConstraintValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = VietnameseFullNameConstraintValidator.class)
@Target({TYPE, FIELD, ANNOTATION_TYPE})
@Retention(RUNTIME)
public @interface ValidVietnameseFullName {

    String message() default "Invalid Data";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

}
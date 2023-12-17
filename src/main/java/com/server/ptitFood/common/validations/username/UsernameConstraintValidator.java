package com.server.ptitFood.common.validations.username;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;


public class UsernameConstraintValidator implements ConstraintValidator<ValidUsername, String> {

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {

        final String regex = "^[a-zA-Z0-9_]{6,20}$";
        Pattern pattern = Pattern.compile(regex);

        if (username == null || username.isEmpty()) {
            return false;
        }

        if (pattern.matcher(username).matches()) {
            return true;
        }

        context.disableDefaultConstraintViolation();

        context.buildConstraintViolationWithTemplate(
                        String.join(" ", "Username is invalid. It must be 6-20 characters and only contains letters, numbers and underscore."))
                .addConstraintViolation();

        return false;
    }
}

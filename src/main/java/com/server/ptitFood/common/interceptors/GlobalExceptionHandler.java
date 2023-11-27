package com.server.ptitFood.common.interceptors;

import com.server.ptitFood.domain.exceptions.UserAlreadyExistException;
import com.server.ptitFood.domain.exceptions.UsernameOrPasswordNotValid;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
//        System.out.println("Handle validation exception");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> onConstraintValidationException(ConstraintViolationException e) {
//        System.out.println("Handle constraint validation exception");
        Map<String, String> errors = new HashMap<>();
        e.getConstraintViolations().forEach((error) -> {
            String fieldName = error.getPropertyPath().toString();
            String errorMessage = error.getMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> onHttpMessageNotReadableException(HttpMessageNotReadableException e) {
//        System.out.println("Handle http message not readable exception");
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Required request body is missing");
        errors.put("status", "400");
        errors.put("error", "Bad Request");
        errors.put("code", GlobalErrorCode.REQUEST_BODY_MISSING);
        return errors;
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> onUserAlreadyExistException(UserAlreadyExistException e) {
//        System.out.println("Handle user already exist exception");
        Map<String, String> errors = new HashMap<>();
        errors.put("message", e.getMessage());
        errors.put("status", "400");
        errors.put("error", "Bad Request");
        errors.put("code", GlobalErrorCode.USER_ALREADY_EXIST);
        return errors;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public Map<String, String> onException(UsernameOrPasswordNotValid e) {
        System.out.println("Handle exception UsernameOrPasswordNotValid");
        Map<String, String> errors = new HashMap<>();
        errors.put("message", "Username or password not valid");
        errors.put("status", "400");
        errors.put("error", "Bad Request");
        errors.put("code", GlobalErrorCode.USER_OR_PASSWORD_INVALID);
        return errors;
    }

}
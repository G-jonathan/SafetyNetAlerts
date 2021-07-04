package com.openClassroomsProject.SafetyNetAlerts.controller;

import com.openClassroomsProject.SafetyNetAlerts.exception.CustomGenericException;
import com.openClassroomsProject.SafetyNetAlerts.exception.ResourceNotFoundException;
import com.openClassroomsProject.SafetyNetAlerts.model.ExceptionMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerControllerAdvice {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomGenericException.class)
    public ExceptionMessage handleRequestCustomGenericException(HttpServletRequest request, CustomGenericException customGenericException) {
        log.error("Exception catch: " + customGenericException.getException().getMessage() + " in " + customGenericException.getLocation());
        return ExceptionMessage.builder()
                .path(request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status("500")
                .error("Internal Server Error")
                .message(customGenericException.getException().getMessage())
                .details("Catch in: " + customGenericException.getLocation())
                .help("Please check Readme here: https://github.com/G-jonathan/SafetyNetAlerts, " +
                        "and see the api documentation at this address: http://localhost:8080/swagger-ui.html#/")
                .build();
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ResourceNotFoundException.class)
    public ExceptionMessage handleRequestNotFoundException(HttpServletRequest request, ResourceNotFoundException resourceNotFoundException) {
        log.error("Exception catch: " + resourceNotFoundException.getMessage() + " in " + resourceNotFoundException.getLocation());
        return ExceptionMessage.builder()
                .path(request.getQueryString() != null ? request.getRequestURI() + "?" + request.getQueryString() : request.getRequestURI())
                .method(request.getMethod())
                .timestamp(LocalDateTime.now().format(dateTimeFormatter))
                .status("404")
                .error("Resource not found")
                .message(resourceNotFoundException.getMessage())
                .details("Catch in: " + resourceNotFoundException.getLocation())
                .help("Please check Readme here: https://github.com/G-jonathan/SafetyNetAlerts, " +
                        "and see the api documentation at this address: http://localhost:8080/swagger-ui.html#/")
                .build();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException methodArgumentNotValidException) {
        log.error("Exception catch: " + methodArgumentNotValidException.getMessage());
        Map<String, String> errors = new HashMap<>();
        methodArgumentNotValidException.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
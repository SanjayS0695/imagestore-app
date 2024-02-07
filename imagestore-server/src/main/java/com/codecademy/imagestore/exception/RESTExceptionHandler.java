package com.codecademy.imagestore.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RESTExceptionHandler {
    protected ResponseEntity<Object> buildResponseEntity(GenericAPIException error) {
        return ResponseEntity.status(error.getStatus()).body(error.getMessage());
    }

    @ExceptionHandler(GenericAPIException.class)
    protected ResponseEntity<Object> handleGenericException(GenericAPIException exception) {
        return buildResponseEntity(exception);
    }
}

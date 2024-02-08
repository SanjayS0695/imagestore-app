package com.codecademy.imagestore.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Global exception handler
 *
 */
@ControllerAdvice
public class RESTExceptionHandler {

    protected ResponseEntity<Object> buildResponseEntity(GenericAPIException error) {
        return ResponseEntity.status(error.getStatus()).body(
                ExceptionResponse.builder()
                        .code(error.getCode())
                        .status(error.getStatus().name())
                        .message(error.getMessage())
                        .build());
    }

    @ExceptionHandler(GenericAPIException.class)
    protected ResponseEntity<Object> handleGenericException(GenericAPIException exception) {
        return buildResponseEntity(exception);
    }
}

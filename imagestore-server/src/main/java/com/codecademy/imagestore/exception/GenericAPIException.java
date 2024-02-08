package com.codecademy.imagestore.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenericAPIException extends RuntimeException {

    private HttpStatus status;
    private String code;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private GenericAPIException() {
        this.timestamp = LocalDateTime.now();
    }

    public GenericAPIException(final ServiceError serviceError, final Object... args) {
        this();
        this.code = serviceError.getCode();
        this.status = serviceError.getStatus();
        if (null != args) {
            this.message = MessageFormat.format(serviceError.getDetails(), args);
        } else {
            this.message = serviceError.getDetails();
        }
    }

    public GenericAPIException(final HttpStatus status, final String message) {
        this();
        this.status = status;
        this.message = message;
    }
}

package com.codecademy.imagestore.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
public class GenericAPIException extends RuntimeException {

    private HttpStatus status;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String debugMessage;

    private GenericAPIException() {
        this.timestamp = LocalDateTime.now();
    }

    public GenericAPIException(HttpStatus status, String message) {
        this();
        this.status = status;
        this.message = message;
    }
}

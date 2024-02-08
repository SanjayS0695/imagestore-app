package com.codecademy.imagestore.exception;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ExceptionResponse {
    private String code;
    private String status;
    private String message;
}

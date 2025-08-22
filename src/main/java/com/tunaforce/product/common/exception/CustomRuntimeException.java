package com.tunaforce.product.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomRuntimeException extends RuntimeException {

    private final HttpStatus status;
    private final String message;

    public CustomRuntimeException(ProductException exception) {
        super(exception.getMessage());

        this.status = exception.getStatus();
        this.message = exception.getMessage();
    }
}

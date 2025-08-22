package com.tunaforce.product.common.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ProductControllerAdvice {

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ExceptionResponseDto> handleRuntimeException(CustomRuntimeException e) {
        return buildResponseEntity(e);
    }

    private ResponseEntity<ExceptionResponseDto> buildResponseEntity(CustomRuntimeException e) {
        return ResponseEntity.status(e.getStatus())
                .body(new ExceptionResponseDto(
                        e.getStatus(),
                        e.getMessage()
                ));
    }
}

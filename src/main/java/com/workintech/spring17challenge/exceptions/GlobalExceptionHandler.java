package com.workintech.spring17challenge.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiErrorResponse> handleApiException(ApiException ex) {
        log.error("Error occurred: {}", ex.getMessage());
        return ResponseEntity.status(ex.getHttpStatus().value())
                .body(new ApiErrorResponse(ex.getHttpStatus().value(), ex.getMessage(), System.currentTimeMillis()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage());
        return ResponseEntity.badRequest()
                .body(new ApiErrorResponse(400, ex.getMessage(), System.currentTimeMillis()));
    }
}

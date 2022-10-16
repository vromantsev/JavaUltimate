package com.bobocode.controller;

import com.bobocode.dto.ErrorResponse;
import com.bobocode.exceptions.PictureNotFoundException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Order(value = Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(value = PictureNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePictureNotFoundException(final PictureNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }
}

package com.bobocode.marslargestpictureredisfeignapp.controller;

import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignException(final FeignException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Cannot receive picture from NASA."));
    }

    record ErrorResponse(String errorMessage) {}
}

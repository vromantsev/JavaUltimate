package com.bobocode.controller;

import com.bobocode.dto.ErrorResponse;
import com.bobocode.exceptions.PictureNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PictureNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePictureNotFoundException(final PictureNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(e.getMessage()));
    }
}

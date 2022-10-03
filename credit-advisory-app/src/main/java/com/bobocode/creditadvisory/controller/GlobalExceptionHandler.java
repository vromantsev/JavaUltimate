package com.bobocode.creditadvisory.controller;

import com.bobocode.creditadvisory.dto.ExceptionResponse;
import com.bobocode.creditadvisory.exception.AdvisorHasAssignedApplicationException;
import com.bobocode.creditadvisory.exception.AdvisorNotFoundException;
import com.bobocode.creditadvisory.exception.ApplicationIsAlreadyAssignedException;
import com.bobocode.creditadvisory.exception.ApplicationNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({AdvisorNotFoundException.class, ApplicationNotFoundException.class})
    public ResponseEntity<ExceptionResponse> handleNotFoundException(final Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ExceptionResponse(e.getMessage()));
    }

    @ExceptionHandler({AdvisorHasAssignedApplicationException.class, ApplicationIsAlreadyAssignedException.class})
    public ResponseEntity<ExceptionResponse> handleEntityStateException(final Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(e.getMessage()));
    }
}

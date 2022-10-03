package com.bobocode.creditadvisory.exception;

public class ApplicationIsAlreadyAssignedException extends AppException {

    public ApplicationIsAlreadyAssignedException(String message) {
        super(message);
    }

    public ApplicationIsAlreadyAssignedException(String message, Throwable cause) {
        super(message, cause);
    }
}

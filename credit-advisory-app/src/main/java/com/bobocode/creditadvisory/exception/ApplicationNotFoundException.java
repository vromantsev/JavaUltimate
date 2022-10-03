package com.bobocode.creditadvisory.exception;

public class ApplicationNotFoundException extends AppException {

    public ApplicationNotFoundException(String message) {
        super(message);
    }

    public ApplicationNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

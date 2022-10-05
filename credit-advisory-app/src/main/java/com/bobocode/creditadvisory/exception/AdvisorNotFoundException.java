package com.bobocode.creditadvisory.exception;

public class AdvisorNotFoundException extends AppException {

    public AdvisorNotFoundException(String message) {
        super(message);
    }

    public AdvisorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

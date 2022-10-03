package com.bobocode.creditadvisory.exception;

public class AdvisorHasAssignedApplicationException extends AppException {

    public AdvisorHasAssignedApplicationException(String message) {
        super(message);
    }

    public AdvisorHasAssignedApplicationException(String message, Throwable cause) {
        super(message, cause);
    }
}

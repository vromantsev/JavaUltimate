package com.bobocode.creditadvisory.exception;

public enum ExceptionMessages {

    ;

    public static final String ADVISOR_HAS_APPLICATION_ASSIGNED_MSG = "Advisor with id=%d already has an application assigned!";
    public static final String PARAMETER_NULL_CHECK_MSG = "Parameter [%s] must not be null!";
    public static final String APPLICATION_IS_ALREADY_ASSIGNED_MSG = "Application with id=%d is already assigned to advisor with id=%d";
    public static final String APPLICATION_IS_NOT_FOUND_MSG = "Application not found!";
    public static final String ADVISOT_IS_NOT_FOUND_MSG = "Advisor with id=%d not found!";
}

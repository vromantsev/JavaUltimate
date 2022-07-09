package com.bobocode.context.exceptions;

/**
 * Is expected to be thrown if nothing is found in a context.
 */
public class NoSuchBeanException extends RuntimeException {

    public NoSuchBeanException(String message) {
        super(message);
    }
}

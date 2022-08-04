package com.bobocode.context.exceptions;

/**
 * Is expected to be thrown if more than one bean is found in a context.
 */
public class NoUniqueBeanException extends RuntimeException {

    public NoUniqueBeanException(String message) {
        super(message);
    }
}

package com.bobocode.context.exceptions;

/**
 * Is expected to be thrown if bean name already exists in context.
 */
public class BeanNameIsNotUnique extends RuntimeException {

    public BeanNameIsNotUnique(String message) {
        super(message);
    }
}

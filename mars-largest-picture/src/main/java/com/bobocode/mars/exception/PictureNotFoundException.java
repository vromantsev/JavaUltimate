package com.bobocode.mars.exception;

public class PictureNotFoundException extends AppException {

    public PictureNotFoundException(String message) {
        super(message);
    }

    public PictureNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

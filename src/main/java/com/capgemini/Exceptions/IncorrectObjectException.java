package com.capgemini.Exceptions;

/**
 * Exception thrown when incorrect object is created.
 */
public class IncorrectObjectException extends RuntimeException {
    public IncorrectObjectException() {
        super();
    }

    public IncorrectObjectException(String message) {
        super(message);
    }
}

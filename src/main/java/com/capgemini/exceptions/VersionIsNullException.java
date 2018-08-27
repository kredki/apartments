package com.capgemini.exceptions;

public class VersionIsNullException extends RuntimeException {
    public VersionIsNullException() {
    }

    public VersionIsNullException(String message) {
        super(message);
    }
}

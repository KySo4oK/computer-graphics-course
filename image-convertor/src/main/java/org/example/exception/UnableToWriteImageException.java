package org.example.exception;

public class UnableToWriteImageException extends RuntimeException {
    public UnableToWriteImageException(String message) {
        super(message);
    }
}

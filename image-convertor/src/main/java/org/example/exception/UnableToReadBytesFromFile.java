package org.example.exception;

public class UnableToReadBytesFromFile extends RuntimeException {
    public UnableToReadBytesFromFile(String message) {
        super(message);
    }
}

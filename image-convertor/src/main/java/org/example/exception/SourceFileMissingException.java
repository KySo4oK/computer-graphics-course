package org.example.exception;

public class SourceFileMissingException extends RuntimeException {
    public SourceFileMissingException(String message) {
        super(message);
    }
}

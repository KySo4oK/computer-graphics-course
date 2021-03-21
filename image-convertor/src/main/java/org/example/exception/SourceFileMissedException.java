package org.example.exception;

public class SourceFileMissedException extends RuntimeException {
    public SourceFileMissedException(String message) {
        super(message);
    }
}

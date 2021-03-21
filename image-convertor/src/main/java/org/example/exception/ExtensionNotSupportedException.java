package org.example.exception;

public class ExtensionNotSupportedException extends RuntimeException {
    public ExtensionNotSupportedException(String message) {
        super(message);
    }
}

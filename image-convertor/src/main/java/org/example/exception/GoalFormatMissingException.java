package org.example.exception;

public class GoalFormatMissingException extends RuntimeException {
    public GoalFormatMissingException(String message) {
        super(message);
    }
}

package org.example.exception;

public class GoalFormatMissedException extends RuntimeException {
    public GoalFormatMissedException(String message) {
        super(message);
    }
}

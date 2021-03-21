package org.example.exception;

import java.io.File;

public class UnableToReadPpmException extends RuntimeException {
    public UnableToReadPpmException(File file) {
        super("Unable to read PPM from " + file.getAbsolutePath());
    }
}

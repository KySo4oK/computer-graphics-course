package org.example.reader.ppm;

import org.example.exception.PpmValidationException;
import org.example.model.ppm.Ppm;
import org.springframework.stereotype.Component;

@Component
public class PpmValidator {
    private static final int MAX_BITS_PER_COLOR = 255;

    public void validate(String magicNumber, int maxColorValue) {
        if (!magicNumber.equals(Ppm.DEFAULT_MAGIC_NUMBER)) {
            throw new PpmValidationException("Magic number is incorrect");
        }
        if (maxColorValue != MAX_BITS_PER_COLOR) {
            throw new PpmValidationException("Max color value exceeds the limit");
        }
    }
}

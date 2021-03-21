package org.example.writer;

import org.example.model.CustomImage;

import java.io.IOException;

public interface ImageWriter {
    void write(CustomImage image, String filePath) throws IOException;
}

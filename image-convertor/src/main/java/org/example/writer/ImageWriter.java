package org.example.writer;

import org.example.model.CustomImage;

public interface ImageWriter {
    void write(CustomImage image, String filePath);
    boolean isSupportedExtension(String extension);
}

package org.example.writer;

import java.io.IOException;
import org.example.model.CustomImage;

public interface ImageWriter {
    void write(CustomImage image, String filePath) throws IOException;
}

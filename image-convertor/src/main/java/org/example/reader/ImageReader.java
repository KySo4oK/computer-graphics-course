package org.example.reader;

import java.io.File;
import org.example.model.CustomImage;

public interface ImageReader {
    CustomImage read(File file);

    boolean isSupportedExtension(String extension);
}

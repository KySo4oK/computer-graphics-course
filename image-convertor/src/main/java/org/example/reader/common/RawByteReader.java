package org.example.reader.common;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.springframework.stereotype.Component;

@Component
public class RawByteReader {
    public byte[] readBytes(File source) {
        try {
            return Files.readAllBytes(Paths.get(source.toURI()));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

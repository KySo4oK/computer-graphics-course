package org.example;

import org.example.reader.ImageReader;
import org.example.writer.ImageWriter;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component
public class ImageConvertor {
    private final List<ImageWriter> writers;
    private final List<ImageReader> readers;

    public ImageConvertor(List<ImageWriter> writers, List<ImageReader> readers) {
        this.writers = writers;
        this.readers = readers;
    }

    void convertImage(final ConsoleConvertCommand convertCommand) throws IOException {
        ImageReader imageReader = getImageReader(convertCommand);
        ImageWriter imageWriter = getImageWriter(convertCommand);
        imageWriter.write(imageReader.read(new File(convertCommand.getSourceFileName())), convertCommand.getOutputFileName());
    }

    private ImageWriter getImageWriter(ConsoleConvertCommand convertCommand) {
        return writers.stream()
                .filter(writer -> writer.isSupportedExtension(convertCommand.getGoalFormat()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }

    private ImageReader getImageReader(ConsoleConvertCommand convertCommand) {
        return readers.stream()
                .filter(reader -> reader.isSupportedExtension(convertCommand.getSourceFileExtension()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }
}

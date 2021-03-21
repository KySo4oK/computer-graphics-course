package org.example;

import java.io.File;
import java.util.List;
import org.example.exception.ExtensionNotSupportedException;
import org.example.reader.ImageReader;
import org.example.writer.ImageWriter;
import org.springframework.stereotype.Component;

@Component
public class ImageConvertor {
    public static final String NOT_SUPPORTED = " - not supported";
    private final List<ImageWriter> writers;
    private final List<ImageReader> readers;

    public ImageConvertor(List<ImageWriter> writers, List<ImageReader> readers) {
        this.writers = writers;
        this.readers = readers;
    }

    void convertImage(final ConsoleConvertCommand convertCommand) {
        ImageReader imageReader = getImageReader(convertCommand);
        ImageWriter imageWriter = getImageWriter(convertCommand);
        imageWriter.write(imageReader.read(new File(convertCommand.getSourceFileName())), convertCommand.getOutputFileName());
    }

    private ImageWriter getImageWriter(ConsoleConvertCommand convertCommand) {
        return writers.stream()
                .filter(writer -> writer.isSupportedExtension(convertCommand.getGoalFormat()))
                .findFirst()
                .orElseThrow(() -> new ExtensionNotSupportedException(convertCommand.getGoalFormat() + NOT_SUPPORTED));
    }

    private ImageReader getImageReader(ConsoleConvertCommand convertCommand) {
        return readers.stream()
                .filter(reader -> reader.isSupportedExtension(convertCommand.getSourceFileExtension()))
                .findFirst()
                .orElseThrow(() -> new ExtensionNotSupportedException(convertCommand.getSourceFileExtension() + NOT_SUPPORTED));
    }
}

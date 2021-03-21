package org.example;

import org.example.reader.bmp.BmpImageReader;
import org.example.reader.ppm.PpmImageReader;
import org.example.writer.ImageWriter;
import org.example.writer.bmp.BmpImageWriter;
import org.example.writer.ppm.PpmImageWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;

@SpringBootApplication
public class App implements CommandLineRunner {
    public static final String PPM = "ppm";
    @Autowired
    private BmpImageReader bmpImageReader;
    @Autowired
    private PpmImageReader ppmImageReader;
    @Autowired
    private BmpImageWriter bmpImageWriter;
    @Autowired
    private PpmImageWriter ppmImageWriter;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        String sourceFileName = getSourceFileName(args);
        String sourceFileExtension = getSourceFileExtension(sourceFileName);
        String goalFormat = getGoalFormat(args);
        String outputFileName = getOutputFileName(args);
        if (sourceFileExtension.equals(PPM)) {
            bmpImageWriter.write(ppmImageReader.read(new File(sourceFileName)), outputFileName);
        } else {
            ppmImageWriter.write(bmpImageReader.read(new File(sourceFileName)), outputFileName);
        }
    }

    private String getOutputFileName(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--output")) {
                return arg.split("=")[1];
            }
        }
        return getDefaultOutputFileName(args);
    }

    private String getDefaultOutputFileName(String[] args) {
        String sourceFileName = getSourceFileName(args);
        return sourceFileName.replaceAll(getSourceFileExtension(sourceFileName), getGoalFormat(args));
    }

    private String getGoalFormat(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--goal-format")) {
                return arg.split("=")[1];
            }
        }
        throw new RuntimeException("goal-format missed");
    }

    private String getSourceFileExtension(String sourceFileName) {
        if (sourceFileName.endsWith(".bmp")) {
            return "bmp";
        } else if (sourceFileName.endsWith(PPM)) {
            return "ppm";
        } else throw new RuntimeException("unsupported extension");
    }

    private String getSourceFileName(String[] args) {
        for (String arg : args) {
            if (arg.startsWith("--source")) {
                return arg.split("=")[1];
            }
        }
        throw new RuntimeException("source file missed");
    }
}

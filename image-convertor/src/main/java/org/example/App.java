package org.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class App implements CommandLineRunner {
    private final ImageConvertor imageConvertor;

    public App(ImageConvertor imageConvertor) {
        this.imageConvertor = imageConvertor;
    }

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(String... args) throws IOException {
        imageConvertor.convertImage(CommandUtils.parseConvertCommand(args));
    }

}

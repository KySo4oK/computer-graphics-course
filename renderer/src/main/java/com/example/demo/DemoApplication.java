package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Triangle[] triangles = ObjLoader.parseFile(new File("cow.obj"));
        Camera camera = new Camera();
        Screen screen = new Screen();
        Pixel[][] pixels = screen.pixels;
        Vector3 origin = camera.getOrigin();
        Vector3[][] races = new Vector3[10][10];
        for (int i = 0; i < races.length; i++) {
            for (int j = 0; j < races[0].length; j++) {
                double x2 = pixels[i][j].x;
                double y2 = pixels[i][j].y;
                double z2 = pixels[i][j].z;
                races[i][j] = new Vector3(x2 - origin.x, y2 - origin.y, z2 - origin.z);
            }
        }


    }
}

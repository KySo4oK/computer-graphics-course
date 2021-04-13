package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DemoApplication.class);

        builder.headless(false);

        ConfigurableApplicationContext context = builder.run(args);
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
        int falses = 0;
        int trues = 0;

        for (Vector3 race : Arrays.stream(races).flatMap(Arrays::stream).collect(Collectors.toList())) {
            for (Triangle triangle : triangles) {
                if (MollerTrumbore.rayIntersectsTriangle(origin, race, triangle, null)) {
                    System.out.println(race);
                    trues++;
                } else {
                    falses++;
                }
            }
        }
        System.out.println(trues);
        System.out.println(falses + "falses");

    }
}

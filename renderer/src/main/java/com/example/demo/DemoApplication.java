package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.io.File;

import static com.example.demo.MollerTrumbore.rayIntersectsTriangle;

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
        Vector3[][] races = new Vector3[pixels.length][pixels[0].length];
        int falses = 0;
        int trues = 0;
        for (int i = 0; i < races.length; i++) {
            for (int j = 0; j < races[0].length; j++) {
                double x2 = pixels[i][j].x;//todo center
                double y2 = pixels[i][j].y;
                double z2 = pixels[i][j].z;
                races[i][j] = new Vector3(x2 - origin.x, y2 - origin.y, z2 - origin.z);
                for (Triangle triangle : triangles) {
                    if (rayIntersectsTriangle(origin, races[i][j], triangle)) {
                        System.out.println(races[i][j]);
                        System.out.println(triangle);
                        trues++;
                        break;
                    } else {
                        falses++;
                    }
                }
            }
        }
        System.out.println(trues);
        System.out.println(falses + "falses");

    }
}

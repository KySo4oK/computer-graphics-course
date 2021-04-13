package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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
        Triangle[] triangles = ObjLoader.parseFile(new File("objects/cubes.obj"));
        Camera camera = new Camera();
        Screen screen = new Screen();
        Pixel[][] pixels = screen.pixels;
        Vector3 origin = camera.getOrigin();
        Vector3[][] races = new Vector3[pixels.length][pixels[0].length];
        BufferedImage image = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_RGB);
        int intersctions = 0;
        int missed = 0;
        for (int i = 0; i < races.length; i++) {
            for (int j = 0; j < races[0].length; j++) {
                double x2 = pixels[i][j].x;//todo center
                double y2 = pixels[i][j].y;
                double z2 = pixels[i][j].z;
                races[i][j] = new Vector3(x2 - origin.x, y2 - origin.y, z2 - origin.z);
                boolean filled = false;

                for (Triangle triangle : triangles) {
                    if (rayIntersectsTriangle(origin, races[i][j], triangle)) {
                        image.setRGB(j, i, Color.CYAN.getRGB());
                        filled = true;
                        intersctions++;
                        break;
                    }
                }
                if (!filled) {
//                    throw new IllegalComponentStateException();
                    image.setRGB(j, i, Color.RED.getRGB());
                    missed++;

                }
            }
        }
        ImageIO.write(image, "png", new File("objects/cubes.png"));
        System.out.println(intersctions);
        System.out.println(missed);

    }
}

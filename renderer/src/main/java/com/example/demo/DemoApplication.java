package com.example.demo;

import com.example.demo.octree.BoundingBox;
import com.example.demo.octree.Octree;
import java.util.Optional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.stream.Collectors;

import static com.example.demo.MollerTrumbore.rayIntersectsTriangle;
import static java.lang.Math.pow;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplicationBuilder builder = new SpringApplicationBuilder(DemoApplication.class);

        builder.headless(false);

        ConfigurableApplicationContext context = builder.run(args);
    }

    @Override
    public void run(String... args) throws Exception {
        ConsoleConvertCommand consoleConvertCommand = CommandUtils.parseConvertCommand(args);
        long before = System.nanoTime();
        LightSource light = new LightSource();
        Triangle[] triangles = ObjLoader.parseFile(new File(consoleConvertCommand.getSourceFileName()));
        BoundingBox boundingBox = new BoundingBox(Arrays.stream(triangles).collect(Collectors.toList()));
        Octree octree = Octree.build(boundingBox, 4);
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
                int fixedJ = pixels[0].length - j - 1;
                int fixedI = pixels.length - i - 1;
               /* for (Triangle triangle : triangles) {
                    if (rayIntersectsTriangle(origin, races[i][j], triangle)) {
                        Vector3 triangleCenter = triangle.getCenterPoint();
                        // найти нормаль к треугольнику
                        Vector3 normal = triangle.normal(triangleCenter);
                        Vector3 lightDirection = new Vector3(
                                light.location.x - triangleCenter.x,
                                light.location.y - triangleCenter.y,
                                light.location.z - triangleCenter.z);
                        Vector3 lightDirection1 = new Vector3(
                                triangleCenter.x - light.location.x,
                                triangleCenter.y - light.location.y,
                                triangleCenter.z - light.location.z);
                        Vector3 subtracted = lightDirection.subtract(normal);
                        double cos = (pow(lightDirection.magnitude(), 2) + pow(normal.magnitude(), 2) - pow(subtracted.magnitude(), 2))
                                / (2 * lightDirection.magnitude() * normal.magnitude());
                        double intensity = Math.max(0.1, cos);
                        image.setRGB(fixedI, fixedJ, multiplyColor(new Color(0, 255, 255), intensity).getRGB());
                        intersctions++;
                        filled = true;
                        intersctions++;
                        break;
                    } else {
                        image.setRGB(fixedI, fixedJ, Color.RED.getRGB());
                    }
                }*/
                Optional<Triangle> visibleTriangleOpt = octree.intersectWithRay(races[i][j], origin);
                if (visibleTriangleOpt.isPresent()) {
                    Triangle visibleTriangle = visibleTriangleOpt.get();
                    Vector3 triangleCenter = visibleTriangle.getCenterPoint();
                    Vector3 normal = visibleTriangle.normal(triangleCenter);
                    Vector3 lightDirection = new Vector3(
                            light.location.x - triangleCenter.x,
                            light.location.y - triangleCenter.y,
                            light.location.z - triangleCenter.z);
                    Vector3 subtracted = lightDirection.subtract(normal);
                    double cos = (pow(lightDirection.magnitude(), 2) + pow(normal.magnitude(), 2) - pow(subtracted.magnitude(), 2))
                            / (2 * lightDirection.magnitude() * normal.magnitude());
                    double intensity = Math.max(0.3, cos);

                    image.setRGB(fixedI, fixedJ, multiplyColor(new Color(0, 255, 255), intensity).getRGB());
                    intersctions++;
                } else {
                    image.setRGB(fixedI, fixedJ, Color.BLACK.getRGB());
                    missed++;
                }
//                if (!filled) {
////                    throw new IllegalComponentStateException();
//                    image.setRGB(fixedI, fixedJ, Color.BLACK.getRGB());
//                    missed++;
//
//                }
            }
        }
        ImageIO.write(image, "png", new File(consoleConvertCommand.getOutputFileName()));
        System.out.println(intersctions);
        System.out.println(missed);
        long after = System.nanoTime();
        System.out.println((after - before) / (1000_000_000.0));
    }

    private Color multiplyColor(Color color, double value) {
        return new Color(
                (int) (color.getRed() * value),
                (int) (color.getGreen() * value),
                (int) (color.getBlue() * value));
    }
}

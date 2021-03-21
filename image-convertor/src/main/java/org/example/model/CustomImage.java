package org.example.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomImage {
    private int height;
    private int width;
    private Pixel[] pixels;

    public Pixel getPixel(int x, int y) {
        return pixels[y * width + x];
    }
}

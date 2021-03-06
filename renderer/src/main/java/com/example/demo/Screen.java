package com.example.demo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Screen {
    final Pixel[][] pixels;

    public Screen() {
        pixels = new Pixel[1000][1000];
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                pixels[i][j] = new Pixel(((double) i)/250.0 - 2,  0.1, ((double) j)/250.0 - 2);
            }
        }
    }
}

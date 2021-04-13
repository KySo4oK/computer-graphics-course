package com.example.demo;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Screen {
    final Pixel[][] pixels;

    public Screen() {
        pixels = new Pixel[1000][1000];
        for (int i = 0; i < pixels.length; i++) {
            for (int j = 0; j < pixels[i].length; j++) {
                pixels[i][j] = new Pixel(i - 5, j - 5, 10);
            }
        }
    }
}

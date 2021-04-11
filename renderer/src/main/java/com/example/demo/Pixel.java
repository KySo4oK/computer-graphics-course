package com.example.demo;

import lombok.Data;

@Data
public class Pixel extends Vector3 {
    private boolean color;
    private static int density = 1;

    public Pixel(double x, double y, double z) {
        super(x, y, z);
    }

    public Vector3 findCenter() {
        double x = (this.x + 0.5) * density;
        double y = (this.y + 0.5) * density;
        return new Vector3(x, y, this.z);
    }
}

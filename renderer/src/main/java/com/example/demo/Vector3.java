package com.example.demo;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Vector3 {

    public double x, y, z;

    public Vector3(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3 copy() {
        return new Vector3(x, y, z);
    }

    public Vector3() {
        this(0, 0, 0);
    }

    public Vector3 add(Vector3 other) {
        return new Vector3(x + other.x, y + other.y, z + other.z);
    }

    public Vector3 subtract(Vector3 other) {
        return new Vector3(x - other.x, y - other.y, z - other.z);
    }

    public double dot(Vector3 other) {
        return x * other.x + y * other.y + z * other.z;
    }

    public Vector3 cross(Vector3 other) {
        return new Vector3(
                y * other.z - z * other.y,
                z * other.x - x * other.z,
                x * other.y - y * other.x
        );
    }

    public Vector3 multiply(Vector3 other) {
        return new Vector3(x * other.x, y * other.y, z * other.z);
    }

    public Vector3 multiply(double scalar) {
        return new Vector3(x * scalar, y * scalar, z * scalar);
    }

    public Vector3 inverse() {
        return multiply(-1);
    }

    public double magnitude() {
        return sqrt(x * x + y * y + z * z);
    }

    public Vector3 normalize() {
        double norm = magnitude();
        return multiply(1 / norm);
    }

    public Vector3 clamp(double min, double max) {
        double x = Math.min(Math.max(min, this.x), max);
        double y = Math.min(Math.max(min, this.y), max);
        double z = Math.min(Math.max(min, this.z), max);
        return new Vector3(x, y, z);
    }

    public double distanceTo(Vector3 other) {
        return sqrt(pow(other.x - x, 2) + pow(other.y - y, 2) + pow(other.z - z, 2));
    }

    public String toString() {
        return String.format("(%3.2f, %3.2f, %3.2f)", x, y, z);
    }

}
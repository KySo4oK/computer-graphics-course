package com.example.demo;


import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Triangle {

    public Vertex v1, v2, v3;
    public Material material;

    public Triangle(Vertex v1, Vertex v2, Vertex v3, Material material) {
        this.material = material;
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public Triangle copy(){
        return new Triangle(v1, v2, v3, material);
    }

    public Vector3 getCenterPoint() {
        return new Vector3(
                (v1.position.x + v2.position.x + v3.position.x) / 3,
                (v1.position.y + v2.position.y + v3.position.y) / 3,
                (v1.position.z + v2.position.z + v3.position.z) / 3);
    }

    public double distanceToOrigin(Vector3 origin) {
        double halfX = (v1.position.x + v2.position.x + v3.position.x) / 3.0;
        double halfY = (v1.position.y + v2.position.y + v3.position.y) / 3.0;
        double halfZ = (v1.position.z + v2.position.z + v3.position.z) / 3.0;
        return sqrt(pow(origin.x - halfX, 2) + pow(origin.y - halfY, 2) + pow(origin.z - halfZ, 2));
    }

    public Vector3 toBarycentricCoordinates(Vector3 point) {
        Vector3 edge1 = this.v2.position.subtract(this.v1.position);
        Vector3 edge2 = this.v3.position.subtract(this.v1.position);
        Vector3 v2 = point.subtract(this.v1.position);

        double d00 = edge1.dot(edge1);
        double d01 = edge1.dot(edge2);
        double d11 = edge2.dot(edge2);

        double d20 = v2.dot(edge1);
        double d21 = v2.dot(edge2);

        double denominator = d00 * d11 - d01 * d01;

        double v = (d11 * d20 - d01 * d21) / denominator;
        double w = (d00 * d21 - d01 * d20) / denominator;
        double u = 1.0 - v - w;
        return new Vector3(u, v, w);
    }

    public Vector3 normal(Vector3 point) {
        Vector3 barycentricCoordinates = toBarycentricCoordinates(point);
        Vector3 normal1 = v1.normal.multiply(barycentricCoordinates.x);
        Vector3 normal2 = v2.normal.multiply(barycentricCoordinates.y);
        Vector3 normal3 = v3.normal.multiply(barycentricCoordinates.z);
        return normal1.add(normal2).add(normal3);
        //return this.v1.normal.normalize();
    }

    public String toString() {
        return String.format("[%s, %s, %s]", this.v1, this.v2, this.v3);
    }

}
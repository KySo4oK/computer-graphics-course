package com.example.demo.octree;

import com.example.demo.Triangle;
import com.example.demo.Vector3;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import static java.lang.Math.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class BoundingBox {
    private double minX;
    private double minY;
    private double minZ;
    private double maxX;
    private double maxY;
    private double maxZ;
    private List<Triangle> triangles = new ArrayList<>();
    private double halfX;
    private double halfY;
    private double halfZ;

    public BoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public BoundingBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, List<Triangle> triangles) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
        this.triangles = triangles;
    }

    public BoundingBox(List<Triangle> triangles) {
        this.triangles = triangles;
        Vector3 someTriangle = triangles.get(0).v1.position;
        minX = someTriangle.x;
        minY = someTriangle.y;
        minZ = someTriangle.z;
        maxX = someTriangle.x;
        maxY = someTriangle.y;
        maxZ = someTriangle.z;
        for (Triangle triangle : triangles) {
            Vector3 v1 = triangle.v1.position;
            Vector3 v2 = triangle.v2.position;
            Vector3 v3 = triangle.v3.position;
            minX = applyFor4(Double::min, minX, v1.x, v2.x, v3.x);
            minY = applyFor4(Double::min, minY, v1.y, v2.y, v3.y);
            minZ = applyFor4(Double::min, minZ, v1.z, v2.z, v3.z);
            maxX = applyFor4(Double::max, maxX, v1.x, v2.x, v3.x);
            maxY = applyFor4(Double::max, maxY, v1.y, v2.y, v3.y);
            maxZ = applyFor4(Double::max, maxZ, v1.z, v2.z, v3.z);
        }
    }

    double applyFor4(BiFunction<Double, Double, Double> function, double... values) {
        return function.apply(function.apply(values[0], values[1]), function.apply(values[2], values[3]));
    }

    void addTriangle(Triangle triangle) {
        triangles.add(triangle);
    }

    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public void setMinZ(double minZ) {
        this.minZ = minZ;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    public void setMaxZ(double maxZ) {
        this.maxZ = maxZ;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
    }

    public List<BoundingBox> divideTo8() {
        halfX = (minX + maxX) / 2;
        halfY = (minY + maxY) / 2;
        halfZ = (minZ + maxZ) / 2;

        List<BoundingBox> boundingBoxes = new ArrayList<>();
        boundingBoxes.add(new BoundingBox(minX, minY, minZ, halfX, halfY, halfZ));
        boundingBoxes.add(new BoundingBox(minX, halfY, minZ, halfX, maxY, halfZ));
        boundingBoxes.add(new BoundingBox(halfX, minY, minZ, maxX, halfY, halfZ));
        boundingBoxes.add(new BoundingBox(minX, minY, halfZ, halfX, halfY, maxZ));
        boundingBoxes.add(new BoundingBox(minX, halfY, halfZ, halfX, maxY, maxZ));
        boundingBoxes.add(new BoundingBox(halfX, minY, halfZ, maxX, halfY, maxZ));
        boundingBoxes.add(new BoundingBox(halfX, halfY, minZ, maxX, maxY, halfZ));
        boundingBoxes.add(new BoundingBox(halfX, halfY, halfZ, maxX, maxY, maxZ));

        List<Triangle> missed = new ArrayList<>();
        for (Triangle triangle : triangles) {
            boolean chosen = false;
            for (BoundingBox boundingBox : boundingBoxes) {
                if (boundingBox.containsTriangle(triangle)) {
                    chosen = true;
                    boundingBox.addTriangle(triangle);
                }
            }
            if (!chosen) {
                missed.add(triangle.copy());
//                throw new RuntimeException();
            }
        }
        this.triangles = missed;
        return boundingBoxes;
    }

    private boolean containsTriangle(Triangle triangle) {
        return Stream.of(triangle.v1.position, triangle.v2.position, triangle.v3.position)
                .allMatch(this::isPointInBox);
    }

    private boolean isPointInBox(Vector3 vector) {
        return (vector.x >= minX && vector.x <= maxX) &&
                (vector.y >= minY && vector.y <= maxY) &&
                (vector.z >= minZ && vector.z <= maxZ);
    }

    public boolean intersect(Vector3 ray, Vector3 origin) {
        // r.dir is unit direction vector of ray
        double dirfracX = 1.0f / ray.x;
        double dirfracY = 1.0f / ray.y;
        double dirfracZ = 1.0f / ray.z;
// lb is the corner of AABB with minimal coordinates - left bottom, rt is maximal corner
// r.org is origin of ray
        double t1 = (minX - origin.x) * dirfracX;
        double t2 = (maxX - origin.x) * dirfracX;
        double t3 = (minY - origin.y) * dirfracY;
        double t4 = (maxY - origin.y) * dirfracY;
        double t5 = (minZ - origin.z) * dirfracZ;
        double t6 = (maxZ - origin.z) * dirfracZ;

        double tmin = max(max(min(t1, t2), min(t3, t4)), min(t5, t6));
        double tmax = min(min(max(t1, t2), max(t3, t4)), max(t5, t6));

// if tmax < 0, ray (line) is intersecting AABB, but the whole AABB is behind us
        if (tmax < 0) {
            return false;
        }

// if tmin > tmax, ray doesn't intersect AABB
        if (tmin > tmax) {
            return false;
        }
        return true;
    }

    double distanceToBox(Vector3 origin) {
        return sqrt(pow(origin.x - halfX, 2) + pow(origin.y - halfY, 2) + pow(origin.z - halfZ, 2));
    }
}

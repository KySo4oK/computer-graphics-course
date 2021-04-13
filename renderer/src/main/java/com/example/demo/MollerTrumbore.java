package com.example.demo;

public class MollerTrumbore {

    private static final double EPSILON = 0.0000001;

    public static boolean rayIntersectsTriangle(Vector3 rayOrigin,
                                                Vector3 rayVector,
                                                Triangle inTriangle,
                                                Vector3 outIntersectionPoint) {
        Vector3 vertex0 = inTriangle.v1.position;
        Vector3 vertex1 = inTriangle.v2.position;
        Vector3 vertex2 = inTriangle.v3.position;
        Vector3 edge1 = new Vector3();
        Vector3 edge2 = new Vector3();
        Vector3 h = new Vector3();
        Vector3 s = new Vector3();
        Vector3 q = new Vector3();
        double a, f, u, v;
        edge1 = vertex1.subtract(vertex0);
        edge2 = vertex2.subtract(vertex0);
        h = rayVector.cross(edge2);
        a = edge1.dot(h);
        if (a > -EPSILON && a < EPSILON) {
            return false;    // This ray is parallel to this triangle.
        }
        f = 1.0 / a;
        s = rayOrigin.subtract(vertex0);
        u = f * (s.dot(h));
        if (u < 0.0 || u > 1.0) {
            return false;
        }
        q = s.cross(edge1);
        v = f * rayVector.dot(q);
        if (v < 0.0 || u + v > 1.0) {
            return false;
        }
        // At this stage we can compute t to find out where the intersection point is on the line.
        double t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            outIntersectionPoint = new Vector3(0.0, 0.0, 0.0);
            outIntersectionPoint = rayVector.multiply(t).add(rayOrigin);
            return true;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return false;
        }
    }
}

package com.example.demo;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

public class MollerTrumbore {

    private static final double EPSILON = 0.0000001;

    public static boolean rayIntersectsTriangle(Point3d rayOrigin,
                                                Vector3d rayVector,
                                                Triangle inTriangle,
                                                Point3d outIntersectionPoint) {
        Point3d vertex0 = inTriangle.v1;
        Point3d vertex1 = inTriangle.v2;
        Point3d vertex2 = inTriangle.v3;
        Vector3d edge1 = new Vector3d();
        Vector3d edge2 = new Vector3d();
        Vector3d h = new Vector3d();
        Vector3d s = new Vector3d();
        Vector3d q = new Vector3d();
        double a, f, u, v;
        edge1.sub(vertex1, vertex0);
        edge2.sub(vertex2, vertex0);
        h.cross(rayVector, edge2);
        a = edge1.dot(h);
        if (a > -EPSILON && a < EPSILON) {
            return false;    // This ray is parallel to this triangle.
        }
        f = 1.0 / a;
        s.sub(rayOrigin, vertex0);
        u = f * (s.dot(h));
        if (u < 0.0 || u > 1.0) {
            return false;
        }
        q.cross(s, edge1);
        v = f * rayVector.dot(q);
        if (v < 0.0 || u + v > 1.0) {
            return false;
        }
        // At this stage we can compute t to find out where the intersection point is on the line.
        double t = f * edge2.dot(q);
        if (t > EPSILON) // ray intersection
        {
            outIntersectionPoint.set(0.0, 0.0, 0.0);
            outIntersectionPoint.scaleAdd(t, rayVector, rayOrigin);
            return true;
        } else // This means that there is a line intersection but not a ray intersection.
        {
            return false;
        }
    }
}

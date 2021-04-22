package com.example.demo.octree;

import com.example.demo.Triangle;
import com.example.demo.Vector3;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.MollerTrumbore.rayIntersectsTriangle;
import static java.lang.Math.ceil;
import static java.util.Collections.emptyList;

public class Octree {
    private final OctreeNode root;
    private final int depth;
    private final List<Octree> children;

    private Octree(OctreeNode root, int depth, List<Octree> children) {
        this.root = root;
        this.depth = depth;
        this.children = children;
    }

    public static Octree build(BoundingBox boundingBox, int depth) {
        if (depth == 1) {
            return new Octree(new OctreeNode(boundingBox, true), depth, emptyList());
        } else {
            List<Octree> children = boundingBox.divideTo8().stream()
                    .map(boundingBox1 -> Octree.build(boundingBox1, depth - 1))
                    .collect(Collectors.toList());
            return new Octree(new OctreeNode(boundingBox, false), depth, children);
        }
    }

    public Optional<Triangle> intersectWithRay(Vector3 ray, Vector3 origin) {
        BoundingBox rootBoundingBox = root.getBoundingBox();
        Triangle goal = null;
        double minDistance = Double.MAX_VALUE;
        if (rootBoundingBox.intersect(ray, origin)) {
            if (root.isLeaf()) {
                for (Triangle triangle : rootBoundingBox.getTriangles()) {
                    if (rayIntersectsTriangle(origin, ray, triangle)) {
                        double distanceToOrigin = triangle.distanceToOrigin(origin);
                        if (minDistance > distanceToOrigin) {
                            goal = triangle;
                            minDistance = distanceToOrigin;
                        }
                    }
                }
                return Optional.ofNullable(goal);
            } else {
                for (Octree child : children) {
                    BoundingBox childBoundingBox = child.root.getBoundingBox();
                    if (childBoundingBox.intersect(ray, origin)) {
                        Optional<Triangle> triangle = child.intersectWithRay(ray, origin);
                        if (triangle.isPresent()) {
                            double distanceToOrigin = triangle.get().distanceToOrigin(origin);
                            if (minDistance > distanceToOrigin) {
                                goal = triangle.get();
                                minDistance = distanceToOrigin;
                            }
                        }
                    }
                }
                for (Triangle triangle : rootBoundingBox.getTriangles()) {
                    if (rayIntersectsTriangle(origin, ray, triangle)) {
                        double distanceToOrigin = triangle.distanceToOrigin(origin);
                        if (minDistance > distanceToOrigin) {
                            goal = triangle;
                            minDistance = distanceToOrigin;
                        }
                    }
                }
            }
        }
        return Optional.ofNullable(goal);
    }

}

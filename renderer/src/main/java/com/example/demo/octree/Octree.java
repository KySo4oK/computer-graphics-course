package com.example.demo.octree;

import com.example.demo.Triangle;
import com.example.demo.Vector3;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.MollerTrumbore.rayIntersectsTriangle;
import static java.util.Collections.emptyList;

public class Octree {
    private OctreeNode root;
    private int depth;
    private List<Octree> children;

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
        BoundingBox boundingBox = root.getBoundingBox();
        if (boundingBox.intersect(ray, origin)) {
            if (root.isLeaf()) {
                for (Triangle triangle : boundingBox.getTriangles()) {
                    if (rayIntersectsTriangle(origin, ray, triangle)) {
                        return Optional.of(triangle);
                    }
                }
                return Optional.empty();
            } else {
                for (Octree child : children) {
                    boundingBox = child.root.getBoundingBox();
                    if (boundingBox.intersect(ray, origin)) {
                        Optional<Triangle> triangle = child.intersectWithRay(ray, origin);
                        if (triangle.isPresent()) {
                            return triangle;
                        }
                    }
                }
                return Optional.empty();
//                throw new RuntimeException("Not intersected with any triangle");
            }
        }
        return Optional.empty();
    }
}

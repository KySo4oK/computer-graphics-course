package com.example.demo.octree;

import com.example.demo.Triangle;
import com.example.demo.Vector3;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.demo.MollerTrumbore.rayIntersectsTriangle;
import static java.lang.Math.ceil;
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
        BoundingBox rootBoundingBox = root.getBoundingBox();
        if (rootBoundingBox.intersect(ray, origin)) {
            if (root.isLeaf()) {
                Optional<Triangle> triangle = intersectWithRootTriangles(ray, origin, rootBoundingBox);
                if (triangle.isPresent()) return triangle;
            } else {
                List<Octree> children = this.children.stream()
                        .sorted((b1, b2) -> (int) (b1.root.getBoundingBox().distanceToBox(origin) - b2.root.getBoundingBox().distanceToBox(origin)))
                        .collect(Collectors.toList());
                for (Octree child : children) {
                    BoundingBox childBoundingBox = child.root.getBoundingBox();
                    if (childBoundingBox.intersect(ray, origin)) {
                        Optional<Triangle> triangle = child.intersectWithRay(ray, origin);
                        if (triangle.isPresent()) {
                            return triangle;
                        }
                    }
                }
                Optional<Triangle> triangle = intersectWithRootTriangles(ray, origin, rootBoundingBox);
                if (triangle.isPresent()) return triangle;
                //                throw new RuntimeException("Not intersected with any triangle");
            }
        }
        return Optional.empty();
    }

    private Optional<Triangle> intersectWithRootTriangles(Vector3 ray, Vector3 origin, BoundingBox rootBoundingBox) {
/*        List<Triangle> triangles = rootBoundingBox.getTriangles().stream()
                .sorted((t1, t2) ->
                        (int) (t2.v2.position.distanceTo(origin)- t1.v2.position.distanceTo(origin)))
                .collect(Collectors.toList());*/
        for (Triangle triangle : rootBoundingBox.getTriangles()) {
            if (rayIntersectsTriangle(origin, ray, triangle)) {
                return Optional.of(triangle);
            }
        }
        return Optional.empty();
    }
}

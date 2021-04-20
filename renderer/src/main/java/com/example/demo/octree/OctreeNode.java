package com.example.demo.octree;

public class OctreeNode {
    private BoundingBox boundingBox;
    private boolean isLeaf;

    public OctreeNode(BoundingBox boundingBox, boolean isLeaf) {
        this.boundingBox = boundingBox;
        this.isLeaf = isLeaf;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public boolean isLeaf() {
        return isLeaf;
    }

    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}

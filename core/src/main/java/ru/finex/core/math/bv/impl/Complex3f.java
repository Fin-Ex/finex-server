package ru.finex.core.math.bv.impl;

import java.util.ArrayList;
import java.util.Collection;
import javax.annotation.Nullable;
import lombok.Getter;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.BoundingVolume3;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.alloc.VectorAllocator;

/**
 * Complex shape consisting of other shapes.
 * This shape doesnt build a bvh to faster search and checks.
 *
 * @author m0nster.mind
 */
public class Complex3f implements BoundingVolume3 {

    @Getter
    private final ArrayList<BoundingVolume3> shapes;
    private Box3f boundingBox;

    public Complex3f(Collection<BoundingVolume3> shapes) {
        this.shapes = new ArrayList<>(shapes);
        this.shapes.trimToSize();
    }

    /**
     * Return bounding aabb of this complex shape.
     * @return bounding aabb
     */
    public Box3f getBoundingBox() {
        if (boundingBox != null) {
            return boundingBox;
        }

        boundingBox = new Box3f();
        for (BoundingVolume3 shape : shapes) {
            if (shape instanceof Box3f box) {
                boundingBox.encapsulate(box.xmax, box.ymax, box.zmax);
                boundingBox.encapsulate(box.xmin, box.ymin, box.zmin);
            } else if (shape instanceof Sphere3f sphere) {
                var min = sphere.getCenter().floatVector()
                    .sub(sphere.getRadius());
                var max = sphere.getCenter().floatVector()
                    .add(sphere.getRadius());

                boundingBox.encapsulate(min.lane(0), min.lane(1), min.lane(2));
                boundingBox.encapsulate(max.lane(0), max.lane(1), max.lane(2));
            } else if (shape instanceof Polygon3f polygon) {
                var polygonBox = polygon.getBox();
                boundingBox.encapsulate(polygonBox.xmax, polygonBox.ymax, polygonBox.zmax);
                boundingBox.encapsulate(polygonBox.xmin, polygonBox.ymin, polygonBox.zmin);
            } else if (shape instanceof Complex3f complex) {
                var complexBox = complex.getBoundingBox();
                boundingBox.encapsulate(complexBox.xmax, complexBox.ymax, complexBox.zmax);
                boundingBox.encapsulate(complexBox.xmin, complexBox.ymin, complexBox.zmin);
            }
        }

        return boundingBox;
    }

    @Override
    public void move(Vector3f point) {
        // TODO oracle: implement this???
    }

    @Override
    public void encapsulate(Vector3f point, VectorAllocator<Vector3f> allocator) {
        // TODO oracle: implement this???
    }

    @Override
    public <T extends BoundingVolume<Vector3f>> void union(T boundingVolume, VectorAllocator<Vector3f> allocator) {
        // TODO oracle: implement this???
    }

    @Override
    public boolean intersects(BoundingVolume3 boundingVolume, VectorAllocator<Vector3f> allocator) {
        if (!getBoundingBox().intersects(boundingVolume, allocator)) {
            return false;
        }

        for (BoundingVolume3 element : shapes) {
            if (element.intersects(boundingVolume, allocator)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void moveCenter(Vector3f point) {
        // TODO oracle: implement this???
    }

    @Override
    public void moveCenter(float x, float y, float z) {
        // TODO oracle: implement this???
    }

    @Override
    public boolean contains(float x, float y, float z) {
        if (!getBoundingBox().contains(x, y, z)) {
            return false;
        }

        for (BoundingVolume3 shape : shapes) {
            if (shape.contains(x, y, z)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(Vector3f point, @Nullable VectorAllocator<Vector3f> allocator) {
        if (!getBoundingBox().contains(point, allocator)) {
            return false;
        }

        for (BoundingVolume3 shape : shapes) {
            if (shape.contains(point, allocator)) {
                return true;
            }
        }

        return false;
    }

}

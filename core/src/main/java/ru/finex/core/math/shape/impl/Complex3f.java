package ru.finex.core.math.shape.impl;

import lombok.Getter;
import ru.finex.core.math.shape.Shape;
import ru.finex.core.math.shape.Shape3;
import ru.finex.core.math.vector.Vector3f;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Complex shape consisting of other shapes.
 * This shape doesnt build a bvh to faster search and checks.
 *
 * @author m0nster.mind
 */
public class Complex3f implements Shape3 {

    @Getter
    private final ArrayList<Shape3> shapes;
    private Box3f boundingBox;

    public Complex3f(Collection<Shape3> shapes) {
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
        for (int i = 0; i < shapes.size(); i++) {
            var shape = shapes.get(i);

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
    public boolean intersects(Shape shape) {
        if (!getBoundingBox().intersects(shape)) {
            return false;
        }

        for (int i = 0; i < shapes.size(); i++) {
            var element = shapes.get(i);
            if (element.intersects(shape)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void moveCenter(Vector3f point) {

    }

    @Override
    public boolean contains(float x, float y, float z) {
        if (!getBoundingBox().contains(x, y, z)) {
            return false;
        }

        for (int i = 0; i < shapes.size(); i++) {
            var shape = shapes.get(i);
            if (shape.contains(x, y, z)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean contains(Vector3f point) {
        if (!getBoundingBox().contains(point)) {
            return false;
        }

        for (int i = 0; i < shapes.size(); i++) {
            var shape = shapes.get(i);
            if (shape.contains(point)) {
                return true;
            }
        }

        return false;
    }
}

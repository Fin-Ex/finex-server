package ru.finex.core.math.shape.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.finex.core.math.shape.Shape;
import ru.finex.core.math.shape.Shape3;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.VectorAllocator;
import ru.finex.core.math.vector.alloc.VectorAllocators;

/**
 * @author mangol
 * @since wgp
 */
@ToString
@EqualsAndHashCode
public class Sphere3f implements Shape3, Cloneable {
    @Getter
    @Setter
    private float radius;

    @Getter
    private final Vector3f center;

    public Sphere3f(Vector3f center, float radius) {
        this.center = center;
        this.radius = radius;
    }

    public Sphere3f(float x, float y, float z, float radius, VectorAllocator<Vector3f> allocator) {
        this.radius = radius;
        center = allocator.alloc().set(x, y, z);
    }

    public Sphere3f(Sphere3f sphere) {
        this(sphere.getCenter().getX(), sphere.getCenter().getY(), sphere.getCenter().getZ(), sphere.getRadius(), VectorAllocators.defaultVector3f());
    }

    /**
     * Move sphere center to specified coordinates.
     * @param x x-axis coordinate
     * @param y y-axis coordinate
     * @param z z-axis coordinate
     */
    public void moveCenter(float x, float y, float z) {
        center.set(x, y, z);
    }

    @Override
    public void moveCenter(Vector3f point) {
        center.set(point);
    }

    @Override
    public boolean contains(float x, float y, float z) {
        return center.distanceSquared(x, y, z) <= radius * radius;
    }

    @Override
    public boolean contains(Vector3f point) {
        return center.distanceSquared(point) <= radius * radius;
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    public boolean intersects(Shape shape) {
        if (shape instanceof Box3f box) {
            return box.intersects(this);
        } else if (shape instanceof Box2f box) {
            return box.intersects(this);
        } else if (shape instanceof Circle2f circle) {
            return circle.intersects(this);
        } else if (shape instanceof Sphere3f sphere) {
            Vector3f center = sphere.getCenter();
            float sumRadius = radius + radius;
            return center.distanceSquared(sphere.center) <= sumRadius * sumRadius;
        }
        return false;
    }

    /**
     * Create a new {@link Box3f} with sphere bounding.
     * @return box
     */
    public Box3f toBox3f() {
        float minX = center.getX() - radius;
        float minY = center.getY() - radius;
        float minZ = center.getZ() - radius;
        float maxX = center.getX() + radius;
        float maxY = center.getY() + radius;
        float maxZ = center.getZ() + radius;
        return new Box3f(minX, maxX, minY, maxY, minZ, maxZ);
    }

    @Override
    public Sphere3f clone() {
        return new Sphere3f(this);
    }
}

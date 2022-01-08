package ru.finex.core.math.shape.impl;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.finex.core.math.shape.Shape;
import ru.finex.core.math.shape.Shape2;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.VectorAllocator;
import ru.finex.core.math.vector.alloc.VectorAllocators;

/**
 * @author mangol
 */
@ToString
@EqualsAndHashCode
public class Circle2f implements Shape2, Cloneable {

    @Getter
    private final Vector2f center;

    @Getter
    @Setter
    private float radius;

    public Circle2f(Vector2f center, float radius) {
        this.radius = radius;
        this.center = center;
    }

    public Circle2f(float x, float y, float radius, VectorAllocator<Vector2f> allocator) {
        this.radius = radius;
        center = allocator.alloc().set(x, y);
    }

    public Circle2f(Circle2f circle) {
        this(circle.getCenter().getX(), circle.getCenter().getY(), circle.getRadius(), VectorAllocators.defaultVector2f());
    }

    /**
     * Set circle center coordinates.
     *
     * @param x x-axis coordinate
     * @param y y-axis coordinate
     */
    public void moveCenter(float x, float y) {
        this.center.set(x, y);
    }

    @Override
    public void moveCenter(Vector3f point) {
        center.set(point.getX(), point.getZ());
    }

    @Override
    public void moveCenter(Vector2f point) {
        center.set(point);
    }

    @Override
    public boolean contains(float x, float y) {
        return center.distanceSquared(x, y) <= radius * radius;
    }

    @Override
    public boolean contains(Vector2f point) {
        return center.distanceSquared(point) <= radius * radius;
    }

    @Override
    public boolean contains(Vector3f point) {
        return contains(point.getX(), point.getZ());
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    public boolean intersects(Shape shape) {
        if (shape instanceof Box2f box) {
            return box.intersects(this);
        } else if (shape instanceof Box3f box) {
            return box.intersects(this);
        } else if (shape instanceof Circle2f circle) {
            float sumRadius = radius + radius;
            return center.distanceSquared(circle.getCenter()) <= sumRadius * sumRadius;
        } else if (shape instanceof Sphere3f sphere) {
            Vector3f sphereCenter = sphere.getCenter();
            float sumRadius = radius + radius;
            float x = center.getX() - sphereCenter.getX();
            float y = center.getY() - sphereCenter.getZ();
            return x * x + y * y <= sumRadius * sumRadius;
        }
        return false;
    }

    /**
     * Create a new {@link Box2f} with circle bounding.
     * @return box
     */
    public Box2f toBox2f() {
        float minX = center.getX() - radius;
        float minY = center.getY() - radius;
        float maxX = center.getX() + radius;
        float maxY = center.getY() + radius;
        return new Box2f(minX, maxX, minY, maxY);
    }

    @Override
    public Circle2f clone() {
        return new Circle2f(this);
    }
}

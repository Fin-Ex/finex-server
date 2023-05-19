package ru.finex.core.math.bv.impl;

import javax.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.BoundingVolume2;
import ru.finex.core.math.bv.BoundingVolume3;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.alloc.VectorAllocator;
import ru.finex.core.math.vector.alloc.VectorAllocators;

/**
 * @author mangol
 * @since wgp
 */
@ToString
@EqualsAndHashCode
public class Circle2f implements BoundingVolume2, Cloneable {

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
    public void move(Vector2f point) {
        // TODO oracle: implement this
    }

    @Override
    public boolean contains(float x, float y) {
        return center.distanceSquared(x, y) <= radius * radius;
    }

    @Override
    public boolean contains(Vector2f point, @Nullable VectorAllocator<Vector2f> allocator) {
        return contains(point);
    }

    public boolean contains(Vector2f point) {
        return center.distanceSquared(point) <= radius * radius;
    }

    @Override
    public boolean contains(Vector3f point, @Nullable VectorAllocator<Vector2f> allocator) {
        return contains(point);
    }

    public boolean contains(Vector3f point) {
        return contains(point.getX(), point.getZ());
    }

    @Override
    @SuppressWarnings("checkstyle:ReturnCount")
    public boolean intersects(BoundingVolume2 boundingVolume, VectorAllocator<Vector2f> allocator) {
        if (boundingVolume instanceof Box2f box) {
            return box.intersects(this, allocator);
        } else if (boundingVolume instanceof OrientedBox2f box) {
            return box.intersects(this, allocator);
        } else if (boundingVolume instanceof Circle2f circle) {
            float sumRadius = radius + radius;
            return center.distanceSquared(circle.getCenter()) <= sumRadius * sumRadius;
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    public boolean intersects(BoundingVolume3 boundingVolume, VectorAllocator<Vector2f> allocator2f,
                              VectorAllocator<Vector3f> allocator3f) {
        if (boundingVolume instanceof Box3f box) {
            return box.intersects(this, allocator2f);
        } else if (boundingVolume instanceof OrientedBox3f box) {
            return box.intersects(boundingVolume, allocator3f);
        } else if (boundingVolume instanceof Sphere3f sphere) {
            Vector3f sphereCenter = sphere.getCenter();
            float sumRadius = radius + radius;
            float x = center.getX() - sphereCenter.getX();
            float y = center.getY() - sphereCenter.getZ();
            return x * x + y * y <= sumRadius * sumRadius;
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    @Override
    public void encapsulate(Vector2f point, VectorAllocator<Vector2f> allocator) {
        // TODO oracle: implement this
    }

    @Override
    public <T extends BoundingVolume<Vector2f>> void union(T boundingVolume, VectorAllocator<Vector2f> allocator) {
        // TODO oracle: implement this
    }

    /**
     * Create a new {@link Box2f} with circle bounding.
     * @return aabb
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

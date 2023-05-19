package ru.finex.core.math.bv.impl;

import javax.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.finex.core.math.FloatVectorMath;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.BoundingVolume2;
import ru.finex.core.math.bv.BoundingVolume3;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.alloc.VectorAllocator;
import ru.finex.core.math.vector.alloc.VectorAllocators;

/**
 * @author mangol
 * @since wgp
 */
@ToString
@EqualsAndHashCode
public class Sphere3f implements BoundingVolume3, Cloneable {

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
    public void move(Vector3f point) {
        // TODO oracle: implement
    }

    @Override
    public boolean contains(float x, float y, float z) {
        return center.distanceSquared(x, y, z) <= radius * radius;
    }

    @Override
    public boolean contains(Vector3f point, @Nullable VectorAllocator<Vector3f> allocator) {
        return contains(point);
    }

    public boolean contains(Vector3f point) {
        return center.distanceSquared(point) <= radius * radius;
    }

    /**
     * Test this sphere to intersect line (line segment).
     * @param startPoint start point of line
     * @param endPoint end point of line
     * @return true if this sphere intersects line, otherwise false
     */
    public boolean intersects(Vector3f startPoint, Vector3f endPoint) {
        var m = startPoint.floatVector().sub(center.floatVector());
        float c = FloatVectorMath.dot128(m, m) - radius * radius;
        if (c <= 0.0f) {
            return true;
        }

        float b = FloatVectorMath.dot128(m, endPoint.floatVector().sub(startPoint.floatVector()));
        if (b > 0.0f) {
            return false;
        }

        // calc discriminant and check
        return b * b - c >= 0.0f;
    }

    @Override
    public boolean intersects(BoundingVolume3 boundingVolume, VectorAllocator<Vector3f> allocator) {
        if (boundingVolume instanceof Box3f box) {
            return box.intersects(this, allocator);
        } else if (boundingVolume instanceof OrientedBox3f box) {
            return box.intersects(this, allocator);
        } else if (boundingVolume instanceof Sphere3f sphere) {
            Vector3f center = sphere.getCenter();
            float sumRadius = radius + radius;
            return center.distanceSquared(sphere.center) <= sumRadius * sumRadius;
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    public boolean intersects(BoundingVolume2 boundingVolume, VectorAllocator<Vector3f> allocator) {
        if (boundingVolume instanceof Box2f box) {
            return box.intersects(this, allocator);
        } else if (boundingVolume instanceof OrientedBox2f box) {
            return box.intersects(this, allocator);
        } else if (boundingVolume instanceof Circle2f circle) {
            return circle.intersects(this, allocator);
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    @Override
    public void encapsulate(Vector3f point, VectorAllocator<Vector3f> allocator) {
        // TODO oracle: implement this???
    }

    @Override
    public <T extends BoundingVolume<Vector3f>> void union(T boundingVolume, VectorAllocator<Vector3f> allocator) {
        // TODO oracle: implement this???
    }

    /**
     * Create a new {@link Box3f} with sphere bounding.
     * @return aabb
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

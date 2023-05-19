package ru.finex.core.math.bv.impl;

import javax.annotation.Nullable;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.BoundingVolume2;
import ru.finex.core.math.bv.BoundingVolume3;
import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.alloc.VectorAllocator;

/**
 * @author m0nster.mind
 * @author oracle
 * @since wgp 29.08.2018
 */
@SuppressWarnings("checkstyle:VisibilityModifier")
@ToString
@EqualsAndHashCode
public class Box2f implements BoundingVolume2, Cloneable {

    public float xmin;
    public float xmax;
    public float ymin;
    public float ymax;

    public Box2f(float xmin, float xmax, float ymin, float ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public Box2f(Vector3f center, float width, float height) {
        xmax = width;
        ymax = height;
        moveCenter(center);
    }

    public Box2f(Box2f other) {
        this(other.xmin, other.xmax, other.ymin, other.ymax);
    }

    public Box2f() {
    }

    /**
     * The aabb width.
     * @return width
     */
    public float getWidth() {
        return xmax - xmin;
    }

    /**
     * The aabb height.
     * @return height
     */
    public float getHeight() {
        return ymax - ymin;
    }

    /**
     * Test this aabb to contain specified point inside.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @return true if this aabb contain specified point inside, otherwise false
     */
    @Override
    public boolean contains(float x, float y) {
        return x >= xmin && x <= xmax && y >= ymin && y <= ymax;
    }

    @Override
    public boolean contains(Vector2f point, @Nullable VectorAllocator<Vector2f> allocator) {
        return contains(point);
    }

    @Override
    public boolean contains(Vector3f point, @Nullable VectorAllocator<Vector2f> allocator) {
        return contains(point);
    }

    /**
     * Test this aabb to contain specified point inside.
     * @param point point
     * @return true if this aabb contain specified point inside, otherwise false
     */
    public boolean contains(Vector2f point) {
        return contains(point.getX(), point.getY());
    }

    /**
     * Test this aabb to contain specified point inside.
     * @param point point
     * @return true if this aabb contain specified point inside, otherwise false
     */
    public boolean contains(Vector3f point) {
        return contains(point.getX(), point.getZ());
    }

    /**
     * Test aabb to intersect circle.
     * @param x x-axis coordinate circle center point
     * @param y y-axis coordinate circle center point
     * @param radius circle radius
     * @return true if aabb intersect circle, otherwise false
     */
    public boolean intersects(float x, float y, float radius) {
        final float sqRadius = radius * radius;

        return isInside(x, y, xmin, ymin, sqRadius) || isInside(x, y, xmin, ymax, sqRadius) ||
            isInside(x, y, xmax, ymin, sqRadius) || isInside(x, y, xmax, ymax, sqRadius);
    }

    /**
     * Test aabb to intersect circle.
     * @param position point of circle center
     * @param radius circle radius
     * @return true if aabb intersect circle, otherwise false
     */
    public boolean intersects(Vector2f position, float radius) {
        return intersects(position.getX(), position.getY(), radius);
    }

    @Override
    public boolean intersects(BoundingVolume2 boundingVolume, VectorAllocator<Vector2f> allocator) {
        if (boundingVolume instanceof Box2f box) {
            return intersectsAABB(xmin, xmax, box.xmin, box.xmax) &&
                intersectsAABB(ymin, ymax, box.ymin, box.ymax);
        } else if (boundingVolume instanceof OrientedBox2f box) {
            return box.intersects(box, allocator);
        } else if (boundingVolume instanceof Circle2f circle) {
            Vector2f center = circle.getCenter();
            return intersects(center.getX(), center.getY(), circle.getRadius());
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    public boolean intersects(BoundingVolume3 boundingVolume, VectorAllocator<Vector3f> allocator) {
        if (boundingVolume instanceof Box3f box) {
            return intersectsAABB(xmin, xmax, box.xmin, box.xmax) &&
                intersectsAABB(ymin, ymax, box.zmin, box.zmax);
        } else if (boundingVolume instanceof OrientedBox3f box) {
            return box.intersects(boundingVolume, allocator);
        } else if (boundingVolume instanceof Sphere3f sphere) {
            Vector3f center = sphere.getCenter();
            return intersects(center.getX(), center.getZ(), sphere.getRadius());
        }

        throw new UnsupportedOperationException("Intersection test with shape [" +
            boundingVolume.getClass().getSimpleName() + "] is not implemented");
    }

    private static boolean isInside(float x1, float y1, float x2, float y2, float sqRadius) {
        final float dx = x2 - x1;
        final float dy = y2 - y1;
        return dx * dx + dy * dy < sqRadius;
    }

    private static boolean intersectsAABB(float a1, float a2, float b1, float b2) {
        return a2 >= b1 && b2 >= a1;
    }

    @Override
    public <T extends BoundingVolume<Vector2f>> void union(T boundingVolume, VectorAllocator<Vector2f> allocator) {
        if (boundingVolume instanceof Box2f box) {
            union(box);
        } else {
            throw new UnsupportedOperationException("Union operation of BV [" +
                boundingVolume.getClass().getSimpleName() "] is not implemented");
        }
    }

    /**
     * Expand aabb to encapsulate a full other aabb.
     * @param other other aabb
     */
    public void union(Box2f other) {
        xmin = Math.min(xmin, other.xmin);
        xmax = Math.max(xmax, other.xmax);
        ymin = Math.min(ymin, other.ymin);
        ymax = Math.max(ymax, other.ymax);
    }

    /**
     * Union two boxes into one.
     * @param b1 first aabb
     * @param b2 second aabb
     * @return union of boxes
     */
    public static Box2f union(Box2f b1, Box2f b2) {
        return new Box2f(
            Math.min(b1.xmin, b2.xmin),
            Math.max(b1.xmax, b2.xmax),
            Math.min(b1.ymin, b2.ymin),
            Math.max(b1.ymax, b2.ymax)
        );
    }

    /**
     * Expand aabb to encapsulate specified point.
     * @param x x-axis coordinate point
     * @param y y-axis coordinate point
     */
    public void encapsulate(float x, float y) {
        xmin = Math.min(xmin, x);
        xmax = Math.max(xmax, x);
        ymin = Math.min(ymin, y);
        ymax = Math.max(ymax, y);
    }

    /**
     * Expand aabb to encapsulate specified point.
     * @param point point
     */
    public void encapsulate(Vector2f point) {
        encapsulate(point.getX(), point.getY());
    }

    @Override
    public void encapsulate(Vector2f point, @Nullable VectorAllocator<Vector2f> allocator) {
        encapsulate(point.getX(), point.getY());
    }

    /**
     * Expand aabb to encapsulate specified point.
     * @param point point
     */
    public void encapsulate(Vector3f point) {
        encapsulate(point.getX(), point.getZ());
    }

    /**
     * Expand aabb by specified value on each side.
     * @param xExtents x-axis expand value
     * @param yExtents y-axis expand value
     */
    public void expand(float xExtents, float yExtents) {
        xmin -= xExtents;
        xmax += yExtents;
        ymin -= yExtents;
        ymax += yExtents;
    }

    /**
     * Move aabb left-upper corner to specified point.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     */
    public void move(float x, float y) {
        xmax = x + getWidth();
        xmin = x;
        ymax = y + getHeight();
        ymin = y;
    }

    /**
     * Move aabb left-upper corner to specified point.
     * @param point point
     */
    public void move(Vector2f point) {
        move(point.getX(), point.getY());
    }

    /**
     * Move aabb left-upper corner to specified point.
     * @param point point
     */
    public void move(Vector3f point) {
        move(point.getX(), point.getZ());
    }

    /**
     * Move aabb center to specified point.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     */
    public void moveCenter(float x, float y) {
        float xExtents = getWidth() / 2f;
        float yExtents = getHeight() / 2f;
        xmin = x - xExtents;
        xmax = x + xExtents;
        ymin = y - yExtents;
        ymax = y + yExtents;
    }

    /**
     * Move aabb center to specified point.
     * @param point point
     */
    @Override
    public void moveCenter(Vector2f point) {
        moveCenter(point.getX(), point.getY());
    }

    /**
     * Move aabb center to specified point.
     * @param point point
     */
    public void moveCenter(Vector3f point) {
        moveCenter(point.getX(), point.getZ());
    }

    /**
     * Compare this aabb and other aabb by X-axis.
     * @param o other aabb
     * @return -1 if this aabb is less, +1 if this aabb is greater, otherwise 0
     */
    public int compareX(Box2f o) {
        return Float.compare(xmin, o.xmin);
    }

    /**
     * Compare this aabb and other aabb by X-axis.
     * @param o other aabb
     * @return -1 if this aabb is less, +1 if this aabb is greater, otherwise 0
     */
    public int compareY(Box2f o) {
        return Float.compare(ymin, o.ymin);
    }

    /**
     * Create new {@link Box2} and copy size and coordinates from this AABB with specified precision.
     * @param precision precision
     * @return Box2
     */
    public Box2 toBox2(int precision) {
        return new Box2(this, precision);
    }

    @Override
    public Box2f clone() {
        return new Box2f(this);
    }

}

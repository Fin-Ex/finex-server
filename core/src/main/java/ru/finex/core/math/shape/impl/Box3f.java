package ru.finex.core.math.shape.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.finex.core.math.ExtMath;
import ru.finex.core.math.shape.Shape;
import ru.finex.core.math.shape.Shape3;
import ru.finex.core.math.vector.Vector3f;

/**
 * @author m0nster.mind
 * @since wgp 25.09.2018
 */
@SuppressWarnings("checkstyle:VisibilityModifier")
@ToString
@EqualsAndHashCode
public class Box3f implements Shape3, Cloneable {

    public float xmin;
    public float xmax;
    public float ymin;
    public float ymax;
    public float zmin;
    public float zmax;

    public Box3f(float xmin, float xmax, float ymin, float ymax, float zmin, float zmax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.zmin = zmin;
        this.zmax = zmax;
    }

    public Box3f() {
        this(Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY,
            Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY,
            Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);
    }

    public Box3f(Box3f box) {
        this(box.xmin, box.xmax, box.ymin, box.ymax, box.zmin, box.zmax);
    }

    public Box3f(Vector3f center, float width, float height, float depth) {
        xmax = width;
        ymax = height;
        zmax = depth;
        moveCenter(center);
    }

    /**
     * The box width.
     * @return width
     */
    public float getWidth() {
        return xmax - xmin;
    }

    /**
     * The box height.
     * @return height
     */
    public float getHeight() {
        return ymax - ymin;
    }

    /**
     * The box depth.
     * @return depth
     */
    public float getDepth() {
        return zmax - zmin;
    }

    /**
     * Expand box to encapsulate specified point.
     * @param x x-axis coordinate point
     * @param y y-axis coordinate point
     * @param z z-axis coordinate point
     */
    public void encapsulate(float x, float y, float z) {
        xmin = Math.min(xmin, x);
        xmax = Math.max(xmax, x);
        ymin = Math.min(ymin, y);
        ymax = Math.max(ymax, y);
        zmin = Math.min(zmin, z);
        zmax = Math.max(zmax, z);
    }

    /**
     * Expand box to encapsulate specified point.
     * @param point point
     */
    public void encapsulate(Vector3f point) {
        encapsulate(point.getX(), point.getY(), point.getZ());
    }

    /**
     * Test this box to contain specified point inside.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @param z z-axis coordinate of point
     * @return true if this box contain specified point inside, otherwise false
     */
    @Override
    public boolean contains(float x, float y, float z) {
        return x >= xmin && x <= xmax && y >= ymin && y <= ymax && z >= zmin && z <= zmax;
    }

    /**
     * Test this box to contain specified point inside.
     * @param point point
     * @return true if this box contain specified point inside, otherwise false
     */
    @Override
    public boolean contains(Vector3f point) {
        return contains(point.getX(), point.getY(), point.getZ());
    }

    /**
     * Test this box to intersect other box.
     * @param box other box
     * @return true if this box intersect other box, otherwise false
     */
    public boolean intersects(Box3f box) {
        return xmin <= box.xmin && xmax >= box.xmin &&
            ymin <= box.ymax && ymax >= box.ymin &&
            zmin <= box.zmax && zmax >= box.zmin;
    }

    /**
     * Test box to intersect sphere.
     * @param x x-axis coordinate sphere center point
     * @param y y-axis coordinate sphere center point
     * @param z z-axis coordinate sphere center point
     * @param radius circle radius
     * @return true if box intersect sphere, otherwise false
     */
    public boolean intersects(float x, float y, float z, float radius) {
        final float bx = Math.max(xmin, Math.min(x, xmax));
        final float by = Math.max(ymin, Math.min(y, ymax));
        final float bz = Math.max(zmin, Math.min(z, zmax));

        final double dx = bx - x;
        final double dy = by - y;
        final double dz = bz - z;

        return Math.sqrt(dx * dx + dy * dy + dz * dz) < radius || contains(x, y, z); // intersects or inside box
    }

    /**
     * Test box to intersect sphere.
     * @param position point of sphere center
     * @param radius circle radius
     * @return true if box intersect sphere, otherwise false
     */
    public boolean intersects(Vector3f position, float radius) {
        return intersects(position.getX(), position.getY(), position.getZ(), radius);
    }

    @SuppressWarnings("checkstyle:ReturnCount")
    @Override
    public boolean intersects(Shape shape) {
        if (shape instanceof Box2f box) {
            return box.intersects(this);
        } else if (shape instanceof Box3f box) {
            return intersectsAABB(xmin, xmax, box.xmin, box.xmax) &&
                intersectsAABB(ymin, ymax, box.ymin, box.ymax) &&
                intersectsAABB(zmin, zmax, box.zmin, box.zmax);
        } else if (shape instanceof Circle2f circle) {
            return circle.intersects(this);
        } else if (shape instanceof Sphere3f sphere) {
            Vector3f center = sphere.getCenter();
            float x = ExtMath.clamp(center.getX(), xmin, xmax);
            float y = ExtMath.clamp(center.getY(), ymin, ymax);
            float z = ExtMath.clamp(center.getZ(), zmin, zmax);
            x -= center.getX();
            y -= center.getY();
            z -= center.getZ();
            return x * x + y * y + z * z < sphere.getRadius() * sphere.getRadius();
        }
        return false;
    }

    private static boolean intersectsAABB(float a1, float a2, float b1, float b2) {
        return a2 >= b1 && b2 >= a1;
    }

    /**
     * Move box center to specified point.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @param z z-axis coordinate of point
     */
    public void moveCenter(float x, float y, float z) {
        float xExtents = getWidth() / 2f;
        float yExtents = getHeight() / 2f;
        float zExtents = getDepth() / 2f;
        xmin = x - xExtents;
        xmax = x + xExtents;
        ymin = y - yExtents;
        ymax = y + yExtents;
        zmin = z - zExtents;
        zmax = z + zExtents;
    }

    @Override
    public void moveCenter(Vector3f point) {
        moveCenter(point.getX(), point.getY(), point.getZ());
    }

    @Override
    public Box3f clone() {
        return new Box3f(this);
    }

}

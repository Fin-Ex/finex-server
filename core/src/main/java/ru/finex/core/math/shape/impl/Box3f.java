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

    private static final float EPSILON_SEGMENT = 0.0001f;

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
     * The aabb depth.
     * @return depth
     */
    public float getDepth() {
        return zmax - zmin;
    }

    /**
     * Expand aabb to encapsulate specified point.
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
     * Expand aabb to encapsulate specified point.
     * @param point point
     */
    public void encapsulate(Vector3f point) {
        encapsulate(point.getX(), point.getY(), point.getZ());
    }

    /**
     * Test this aabb to contain specified point inside.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @param z z-axis coordinate of point
     * @return true if this aabb contain specified point inside, otherwise false
     */
    @Override
    public boolean contains(float x, float y, float z) {
        return x >= xmin && x <= xmax && y >= ymin && y <= ymax && z >= zmin && z <= zmax;
    }

    /**
     * Test this aabb to contain specified point inside.
     * @param point point
     * @return true if this aabb contain specified point inside, otherwise false
     */
    @Override
    public boolean contains(Vector3f point) {
        return contains(point.getX(), point.getY(), point.getZ());
    }

    /**
     * Test this aabb to intersect line (line segment).
     * @param startPoint start point of line
     * @param endPoint end point of line
     * @return true if this aabb intersect line, otherwise false
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public boolean intersects(Vector3f startPoint, Vector3f endPoint) {
        // center of the aabb
        float bcx = (xmin + xmax) * 0.5f;
        float bcy = (ymin + ymax) * 0.5f;
        float bcz = (zmin + zmax) * 0.5f;

        // aabb half extents
        float bex = xmax - bcx;
        float bey = ymax - bcy;
        float bez = zmax - bcz;

        // segment midpoint
        float smx = (startPoint.getX() + endPoint.getX()) * 0.5f;
        float smy = (startPoint.getY() + endPoint.getY()) * 0.5f;
        float smz = (startPoint.getZ() + endPoint.getZ()) * 0.5f;

        // segment half extents
        float sex = startPoint.getX() - smx;
        float sey = startPoint.getY() - smy;
        float sez = startPoint.getZ() - smz;

        // translate
        smx -= bcx;
        smy -= bcy;
        smz -= bcz;

        float adx = Math.abs(sex);
        if (axisSegmentBox(smx, bex, adx)) {
            return false;
        }

        float ady = Math.abs(sey);
        if (axisSegmentBox(smy, bey, ady)) {
            return false;
        }

        float adz = Math.abs(sez);
        if (axisSegmentBox(smz, bez, adz)) {
            return false;
        }

        // correct math error when segment is parallel to coordinate axis
        adx += EPSILON_SEGMENT;
        ady += EPSILON_SEGMENT;
        adz += EPSILON_SEGMENT;

        // cross product
        return Math.abs(smy * sez - smz * sey) > bey * adz + bez * ady ||
            Math.abs(smz * sex + smx * sez) > bex * adz + bez * adx ||
            Math.abs(smx * sey - smy * sex) > bex * ady + bey * adx;
    }

    /**
     * Test this aabb to intersect other aabb.
     * @param box other aabb
     * @return true if this aabb intersect other aabb, otherwise false
     */
    public boolean intersects(Box3f box) {
        return xmin <= box.xmin && xmax >= box.xmin &&
            ymin <= box.ymax && ymax >= box.ymin &&
            zmin <= box.zmax && zmax >= box.zmin;
    }

    /**
     * Test aabb to intersect sphere.
     * @param x x-axis coordinate sphere center point
     * @param y y-axis coordinate sphere center point
     * @param z z-axis coordinate sphere center point
     * @param radius circle radius
     * @return true if aabb intersect sphere, otherwise false
     */
    public boolean intersects(float x, float y, float z, float radius) {
        float bx = ExtMath.clamp(x, xmin, xmax);
        float by = ExtMath.clamp(y, ymin, ymax);
        float bz = ExtMath.clamp(z, zmin, zmax);

        double dx = bx - x;
        double dy = by - y;
        double dz = bz - z;

        return Math.sqrt(dx * dx + dy * dy + dz * dz) < radius || contains(x, y, z); // intersects or inside aabb
    }

    /**
     * Test aabb to intersect sphere.
     * @param position point of sphere center
     * @param radius circle radius
     * @return true if aabb intersect sphere, otherwise false
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
            return intersects(center.getX(), center.getY(), center.getZ(), sphere.getRadius());
        }
        return false;
    }

    private static boolean intersectsAABB(float a1, float a2, float b1, float b2) {
        return a2 >= b1 && b2 >= a1;
    }

    private static boolean axisSegmentBox(float sm, float be, float ad) {
        return Math.abs(sm) > be + ad;
    }

    /**
     * Move aabb center to specified point.
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

    /**
     * Create new {@link Box3} and copy size and coordinates from this AABB with specified precision.
     * @param precision precision
     * @return Box3
     */
    public Box3 toBox3(int precision) {
        return new Box3(this, precision);
    }

    @Override
    public Box3f clone() {
        return new Box3f(this);
    }

}

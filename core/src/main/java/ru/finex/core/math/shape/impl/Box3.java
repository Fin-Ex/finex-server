package ru.finex.core.math.shape.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.finex.core.math.vector.Vector3f;

/**
 * Integer-based 3D AABB shape.
 *
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:VisibilityModifier")
@ToString
@EqualsAndHashCode
public class Box3 {

    public int xmin;
    public int xmax;
    public int ymin;
    public int ymax;
    public int zmin;
    public int zmax;

    public Box3(int xmin, int xmax, int ymin, int ymax, int zmin, int zmax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
        this.zmin = zmin;
        this.zmax = zmax;
    }

    public Box3(Box3 other) {
        this(other.xmin, other.xmax, other.ymin, other.ymax, other.zmin, other.zmax);
    }

    public Box3(Box3f other, int precision) {
        xmax = (int) (other.xmax * precision);
        xmin = (int) (other.xmin * precision);
        ymax = (int) (other.ymax * precision);
        ymin = (int) (other.ymin * precision);
        zmax = (int) (other.zmax * precision);
        zmin = (int) (other.zmin * precision);
    }

    public Box3() {
    }

    /**
     * Copy AABB size and coordinates from other AABB to this AABB.
     * @param box other AABB
     * @return this
     */
    public Box3 set(Box3 box) {
        this.xmin = box.xmin;
        this.xmax = box.xmax;
        this.ymin = box.ymin;
        this.ymax = box.ymax;
        this.zmin = box.zmin;
        this.zmax = box.zmax;
        return this;
    }

    /**
     * Copy AABB size and coordinates from other AABB to this AABB with specified precision.
     * @param box other AABB
     * @param precision precision
     * @return this
     */
    public Box3 set(Box3f box, int precision) {
        xmax = (int) (box.xmax * precision);
        xmin = (int) (box.xmin * precision);
        ymax = (int) (box.ymax * precision);
        ymin = (int) (box.ymin * precision);
        zmax = (int) (box.zmax * precision);
        zmin = (int) (box.zmin * precision);
        return this;
    }

    /**
     * The AABB width.
     * @return width
     */
    public int getWidth() {
        return xmax - xmin;
    }

    /**
     * The AABB height.
     * @return height
     */
    public int getHeight() {
        return ymax - ymin;
    }

    /**
     * The AABB depth.
     * @return depth
     */
    public int getDepth() {
        return zmax - zmin;
    }

    /**
     * Test this AABB to contain specified point inside.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @param z z-axis coordinate of point
     * @return true if this AABB contain specified point inside, otherwise false
     */
    public boolean contains(int x, int y, int z) {
        return x >= xmin && x <= xmax && y >= ymin && y <= ymax && z >= zmin && z <= zmax;
    }

    /**
     * Test this AABB to intersect other AABB.
     * @param box other AABB
     * @return true if this AABB intersect other AABB, otherwise false
     */
    public boolean intersects(Box3 box) {
        return box.xmax >= xmin && box.xmin <= xmax &&
            box.ymax >= ymin && box.ymin <= ymax &&
            box.zmax >= zmax && box.zmin <= zmin;
    }

    /**
     * Test AABB to intersect circle.
     * @param x x-axis coordinate circle center point
     * @param y y-axis coordinate circle center point
     * @param z z-axis coordinate circle center point
     * @param radius circle radius
     * @return true if AABB intersect circle, otherwise false
     */
    public boolean intersects(int x, int y, int z, int radius) {
        final long sqRadius = radius * radius;
        return internalIntersects(x, y, z, sqRadius);
    }

    /**
     * Test AABB to intersect circle.
     * @param x x-axis coordinate circle center point
     * @param y y-axis coordinate circle center point
     * @param z z-axis coordinate circle center point
     * @param sqRadius squared radius
     * @return true if AABB intersect circle, otherwise false
     */
    public boolean internalIntersects(int x, int y, int z, long sqRadius) {
        return isInside(x, y, z, xmin, ymin, zmin, sqRadius) ||
            isInside(x, y, z, xmin, ymax, zmin, sqRadius) ||
            isInside(x, y, z, xmin, ymin, zmax, sqRadius) ||
            isInside(x, y, z, xmin, ymax, zmax, sqRadius) ||
            isInside(x, y, z, xmax, ymin, zmin, sqRadius) ||
            isInside(x, y, z, xmax, ymax, zmin, sqRadius) ||
            isInside(x, y, z, xmax, ymin, zmax, sqRadius) ||
            isInside(x, y, z, xmax, ymax, zmax, sqRadius);
    }

    private static boolean isInside(int x1, int y1, int z1, int x2, int y2, int z2, long sqRadius) {
        long dx = x2 - x1;
        long dy = y2 - y1;
        long dz = z2 - z1;
        return dx * dx + dy * dy + dz * dz < sqRadius;
    }

    /**
     * Expand AABB to encapsulate a full other AABB.
     * @param other other AABB
     */
    public void union(Box3 other) {
        xmin = Math.min(xmin, other.xmin);
        xmax = Math.max(xmax, other.xmax);
        ymin = Math.min(ymin, other.ymin);
        ymax = Math.max(ymax, other.ymax);
        zmin = Math.min(zmin, other.zmin);
        zmax = Math.max(zmax, other.zmax);
    }

    /**
     * Union two boxes into one.
     * @param b1 first AABB
     * @param b2 second AABB
     * @return union of boxes
     */
    public static Box3 union(Box3 b1, Box3 b2) {
        return new Box3(
            Math.min(b1.xmin, b2.xmin),
            Math.max(b1.xmax, b2.xmax),
            Math.min(b1.ymin, b2.ymin),
            Math.max(b1.ymax, b2.ymax),
            Math.min(b1.zmin, b2.zmin),
            Math.max(b1.zmax, b2.zmax)
        );
    }

    /**
     * Expand AABB to encapsulate specified point.
     * @param x x-axis coordinate point
     * @param y y-axis coordinate point
     */
    public void encapsulate(int x, int y) {
        xmin = Math.min(xmin, x);
        xmax = Math.max(xmax, x);
        ymin = Math.min(ymin, y);
        ymax = Math.max(ymax, y);
    }

    /**
     * Expand AABB by specified value on each side.
     * @param xExtents x-axis expand value
     * @param yExtents y-axis expand value
     * @param zExtents z-axis expand value
     */
    public void expand(int xExtents, int yExtents, int zExtents) {
        xmin -= xExtents;
        xmax += yExtents;
        ymin -= yExtents;
        ymax += yExtents;
        zmin -= zExtents;
        zmax += zExtents;
    }

    /**
     * Move AABB left-upper corner to specified point.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @param z z-axis coordinate of point
     */
    public void move(int x, int y, int z) {
        xmax = x + getWidth();
        xmin = x;
        ymax = y + getHeight();
        ymin = y;
        zmax = z + getDepth();
        zmin = z;
    }

    /**
     * Move AABB center to specified point.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @param z z-axis coordinate of point
     */
    public void moveCenter(int x, int y, int z) {
        int xExtents = getWidth() / 2;
        int yExtents = getHeight() / 2;
        int zExtents = getDepth() / 2;
        xmin = x - xExtents;
        xmax = x + xExtents;
        ymin = y - yExtents;
        ymax = y + yExtents;
        zmin = z - zExtents;
        zmax = z + zExtents;
    }

    /**
     * Move AABB center to specified point.
     * @param point point
     * @param precision aabb precision
     */
    public void moveCenter(Vector3f point, int precision) {
        moveCenter((int) (point.getX() * precision), (int) (point.getY() * precision), (int) (point.getZ() * precision));
    }

    /**
     * Compare this AABB and other AABB by X-axis.
     * @param o other AABB
     * @return -1 if this AABB is less, +1 if this AABB is greater, otherwise 0
     */
    public int compareX(Box3 o) {
        return Integer.compare(xmin, o.xmin);
    }

    /**
     * Compare this AABB and other AABB by Y-axis.
     * @param o other AABB
     * @return -1 if this AABB is less, +1 if this AABB is greater, otherwise 0
     */
    public int compareY(Box3 o) {
        return Integer.compare(ymin, o.ymin);
    }

    /**
     * Compare this AABB and other AABB by Y-axis.
     * @param o other AABB
     * @return -1 if this AABB is less, +1 if this AABB is greater, otherwise 0
     */
    public int compareZ(Box3 o) {
        return Integer.compare(zmin, o.zmin);
    }
}

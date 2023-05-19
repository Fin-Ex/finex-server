package ru.finex.core.math.bv.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.finex.core.math.vector.Vector3f;

/**
 * Integer-based 2D aabb shape.
 *
 * @author m0nster.mind
 * @since wgp 29.08.2018
 */
@SuppressWarnings("checkstyle:VisibilityModifier")
@ToString
@EqualsAndHashCode
public class Box2 {

    public int xmin;
    public int xmax;
    public int ymin;
    public int ymax;

    public Box2(int xmin, int xmax, int ymin, int ymax) {
        this.xmin = xmin;
        this.xmax = xmax;
        this.ymin = ymin;
        this.ymax = ymax;
    }

    public Box2(Box2 other) {
        this(other.xmin, other.xmax, other.ymin, other.ymax);
    }

    public Box2(Box2f other, int precision) {
        xmax = (int) (other.xmax * precision);
        xmin = (int) (other.xmin * precision);
        ymax = (int) (other.ymax * precision);
        ymin = (int) (other.ymin * precision);
    }

    public Box2() {
    }

    /**
     * Copy aabb size and coordinates from other aabb to this aabb.
     * @param box other aabb
     * @return this
     */
    public Box2 set(Box2 box) {
        this.xmin = box.xmin;
        this.xmax = box.xmax;
        this.ymin = box.ymin;
        this.ymax = box.ymax;
        return this;
    }

    /**
     * Copy aabb size and coordinates from other aabb to this aabb with specified precision.
     * @param box other aabb
     * @param precision precision
     * @return this
     */
    public Box2 set(Box2f box, int precision) {
        xmax = (int) (box.xmax * precision);
        xmin = (int) (box.xmin * precision);
        ymax = (int) (box.ymax * precision);
        ymin = (int) (box.ymin * precision);
        return this;
    }

    /**
     * The aabb width.
     * @return width
     */
    public int getWidth() {
        return xmax - xmin;
    }

    /**
     * The aabb height.
     * @return height
     */
    public int getHeight() {
        return ymax - ymin;
    }

    /**
     * Test this aabb to contain specified point inside.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @return true if this aabb contain specified point inside, otherwise false
     */
    public boolean contains(int x, int y) {
        return x >= xmin && x <= xmax && y >= ymin && y <= ymax;
    }

    /**
     * Test this aabb to intersect other aabb.
     * @param box other aabb
     * @return true if this aabb intersect other aabb, otherwise false
     */
    public boolean intersects(Box2 box) {
        return box.xmax >= xmin && box.xmin <= xmax &&
            box.ymax >= ymin && box.ymin <= ymax;
    }

    /**
     * Test aabb to intersect circle.
     * @param x x-axis coordinate circle center point
     * @param y y-axis coordinate circle center point
     * @param radius circle radius
     * @return true if aabb intersect circle, otherwise false
     */
    public boolean intersects(int x, int y, int radius) {
        final long sqRadius = radius * radius;
        return internalIntersects(x, y, sqRadius);
    }

    /**
     * Test aabb to intersect circle.
     * @param x x-axis coordinate circle center point
     * @param y y-axis coordinate circle center point
     * @param sqRadius squared radius
     * @return true if aabb intersect circle, otherwise false
     */
    public boolean internalIntersects(int x, int y, long sqRadius) {
        return isInside(x, y, xmin, ymin, sqRadius) || isInside(x, y, xmin, ymax, sqRadius) ||
            isInside(x, y, xmax, ymin, sqRadius) || isInside(x, y, xmax, ymax, sqRadius);
    }

    private static boolean isInside(int x1, int y1, int x2, int y2, long sqRadius) {
        final long dx = x2 - x1;
        final long dy = y2 - y1;
        return dx * dx + dy * dy < sqRadius;
    }

    /**
     * Expand aabb to encapsulate a full other aabb.
     * @param other other aabb
     */
    public void union(Box2 other) {
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
    public static Box2 union(Box2 b1, Box2 b2) {
        return new Box2(
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
    public void encapsulate(int x, int y) {
        xmin = Math.min(xmin, x);
        xmax = Math.max(xmax, x);
        ymin = Math.min(ymin, y);
        ymax = Math.max(ymax, y);
    }

    /**
     * Expand aabb by specified value on each side.
     * @param xExtents x-axis expand value
     * @param yExtents y-axis expand value
     */
    public void expand(int xExtents, int yExtents) {
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
    public void move(int x, int y) {
        xmax = x + getWidth();
        xmin = x;
        ymax = y + getHeight();
        ymin = y;
    }

    /**
     * Move aabb center to specified point.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     */
    public void moveCenter(int x, int y) {
        int xExtents = getWidth() / 2;
        int yExtents = getHeight() / 2;
        xmin = x - xExtents;
        xmax = x + xExtents;
        ymin = y - yExtents;
        ymax = y + yExtents;
    }

    /**
     * Move aabb center to specified point.
     * @param point point
     * @param precision aabb precision
     */
    public void moveCenter(Vector3f point, int precision) {
        moveCenter((int) (point.getX() * precision), (int) (point.getZ() * precision));
    }

    /**
     * Compare this aabb and other aabb by X-axis.
     * @param o other aabb
     * @return -1 if this aabb is less, +1 if this aabb is greater, otherwise 0
     */
    public int compareX(Box2 o) {
        return Integer.compare(xmin, o.xmin);
    }

    /**
     * Compare this aabb and other aabb by Y-axis.
     * @param o other aabb
     * @return -1 if this aabb is less, +1 if this aabb is greater, otherwise 0
     */
    public int compareY(Box2 o) {
        return Integer.compare(ymin, o.ymin);
    }
}

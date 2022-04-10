package ru.finex.core.math.shape.impl;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import ru.finex.core.math.vector.Vector3f;

/**
 * Integer-based 2D box shape.
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
     * Copy box size & coordinates from other box to this box.
     * @param box other box
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
     * Copy box size & coordinates from other box to this box with specified precision.
     * @param box other box
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
     * The box width.
     * @return width
     */
    public int getWidth() {
        return xmax - xmin;
    }

    /**
     * The box height.
     * @return height
     */
    public int getHeight() {
        return ymax - ymin;
    }

    /**
     * Test this box to contain specified point inside.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @return true if this box contain specified point inside, otherwise false
     */
    public boolean contains(int x, int y) {
        return x >= xmin && x <= xmax && y >= ymin && y <= ymax;
    }

    /**
     * Test this box to intersect other box.
     * @param box other box
     * @return true if this box intersect other box, otherwise false
     */
    public boolean intersects(Box2 box) {
        return box.xmax >= xmin && box.xmin <= xmax &&
            box.ymax >= ymin && box.ymin <= ymax;
    }

    /**
     * Test box to intersect circle.
     * @param x x-axis coordinate circle center point
     * @param y y-axis coordinate circle center point
     * @param radius circle radius
     * @return true if box intersect circle, otherwise false
     */
    public boolean intersects(int x, int y, int radius) {
        final long sqRadius = radius * radius;

        return isInside(x, y, xmin, ymin, sqRadius) || isInside(x, y, xmin, ymax, sqRadius) ||
            isInside(x, y, xmax, ymin, sqRadius) || isInside(x, y, xmax, ymax, sqRadius);
    }

    /**
     * Test box to intersect circle.
     * @param x x-axis coordinate circle center point
     * @param y y-axis coordinate circle center point
     * @param sqRadius squared radius
     * @return true if box intersect circle, otherwise false
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
     * Expand box to encapsulate a full other box.
     * @param other other box
     */
    public void union(Box2 other) {
        xmin = Math.min(xmin, other.xmin);
        xmax = Math.max(xmax, other.xmax);
        ymin = Math.min(ymin, other.ymin);
        ymax = Math.max(ymax, other.ymax);
    }

    /**
     * Union two boxes into one.
     * @param b1 first box
     * @param b2 second box
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
     * Expand box to encapsulate specified point.
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
     * Expand box by specified value on each side.
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
     * Move box left-upper corner to specified point.
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
     * Move box center to specified point.
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
     * Move box center to specified point.
     * @param point point
     * @param precision box precision
     */
    public void moveCenter(Vector3f point, int precision) {
        moveCenter((int) (point.getX() * precision), (int) (point.getZ() * precision));
    }

    /**
     * Compare this box and other box by X-axis.
     * @param o other box
     * @return -1 if this box is less, +1 if this box is greater, otherwise 0
     */
    public int compareX(Box2 o) {
        return Integer.compare(xmin, o.xmin);
    }

    /**
     * Compare this box and other box by Y-axis.
     * @param o other box
     * @return -1 if this box is less, +1 if this box is greater, otherwise 0
     */
    public int compareY(Box2 o) {
        return Integer.compare(ymin, o.ymin);
    }
}

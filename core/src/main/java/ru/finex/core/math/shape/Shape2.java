package ru.finex.core.math.shape;

import ru.finex.core.math.vector.Vector2f;
import ru.finex.core.math.vector.Vector3f;

/**
 * Base 2D shape interface.
 *
 * @author m0nster.mind
 * @author mangol
 * @since wgp
 */
public interface Shape2 extends Shape {

    /**
     * Test this shape to contain specified point inside.
     *
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @return return true if shape contains specified point inside, otherwise false
     */
    boolean contains(float x, float y);

    /**
     * Test this shape to containt specified point inside.
     * @param point point
     * @return return true if shape contains specified point inside, otherwise false
     */
    boolean contains(Vector2f point);

    /**
     * Test this shape to containt specified point inside.
     * @param point point
     * @return return true if shape contains specified point inside, otherwise false
     */
    boolean contains(Vector3f point);

    /**
     * Move shape center to point coordinates.
     * @param point point
     */
    void moveCenter(Vector2f point);

}

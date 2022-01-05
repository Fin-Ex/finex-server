package ru.finex.core.math.shape;

import ru.finex.core.math.vector.Vector3f;

/**
 * Base 3D shape interface.
 *
 * @author m0nster.mind
 * @author mangol
 * @since wgp
 */
public interface Shape3 extends Shape {

    /**
     * Test shape to contain specified point inside.
     * @param x x-axis coordinate of point
     * @param y y-axis coordinate of point
     * @param z z-axis coordinate of point
     * @return return true if shape contains specified point inside, otherwise false
     */
    boolean contains(float x, float y, float z);

    /**
     * Test shape to contain specified point inside.
     * @param point point
     * @return return true if shape contains specified point inside, otherwise false
     */
    boolean contains(Vector3f point);

}

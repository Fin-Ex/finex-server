package ru.finex.core.math.shape;

import ru.finex.core.math.vector.Vector3f;

/**
 * Base shape interface.
 *
 * @author m0nster.mind
 * @author mangol
 * @since wgp
 */
public interface Shape {

    /**
     * Test this shape to intersection other shape.
     * @param shape shape
     * @return if this shape intersect other shape return true, otherwise false
     */
    boolean intersects(Shape shape);

    /**
     * Move shape center to point coordinates.
     * @param point point
     */
    void moveCenter(Vector3f point);

}

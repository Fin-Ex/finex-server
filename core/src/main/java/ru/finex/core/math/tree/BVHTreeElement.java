package ru.finex.core.math.tree;

import ru.finex.core.math.shape.impl.Box2f;

/**
 * @author m0nster.mind
 */
public interface BVHTreeElement {

    /**
     * Unique ID to store in BVH.
     *
     * @return ID
     */
    int getId();

    /**
     * Bounding box of object.
     *
     * @return bounding box
     */
    Box2f getBoundingBox();

}

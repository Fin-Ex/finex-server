package ru.finex.core.math.bvh.aabb;

import ru.finex.core.math.bv.impl.Box2f;

/**
 * @author m0nster.mind
 */
public interface Box2TreeElement {

    /**
     * Unique ID to store in BVH.
     *
     * @return ID
     */
    int getId();

    /**
     * Bounding AABB of object.
     *
     * @return AABB
     */
    Box2f getBoundingBox();

}

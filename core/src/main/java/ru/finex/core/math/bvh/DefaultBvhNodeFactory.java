package ru.finex.core.math.bvh;

import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.bv.impl.Box2f;
import ru.finex.core.math.bv.impl.Box3f;
import ru.finex.core.math.bv.impl.OrientedBox2f;
import ru.finex.core.math.bv.impl.OrientedBox3f;

/**
 * @author oracle
 */
public class DefaultBvhNodeFactory implements BvhNodeFactory {

    @Override
    public BvhNode<?, ?> createBvhNode(BoundingVolume<?> boundingVolume) {
        if (Box2f.class.isAssignableFrom(boundingVolume.getClass())) {
            return new AbstractBvhNode<>((Box2f) boundingVolume) {};
        } else if (Box3f.class.isAssignableFrom(boundingVolume.getClass())) {
            //return new AbstractBvhNode<>((Box3f) boundingVolume) {};
            return null;
        } else if (OrientedBox2f.class.isAssignableFrom(boundingVolume.getClass())) {
            return new AbstractBvhNode<>((OrientedBox2f) boundingVolume) {};
        } else if (OrientedBox3f.class.isAssignableFrom(boundingVolume.getClass())) {
            return new AbstractBvhNode<>((OrientedBox3f) boundingVolume) {};
        }

        // todo oracle: message
        throw new IllegalArgumentException("");
    }

}

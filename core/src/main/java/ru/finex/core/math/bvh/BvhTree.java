package ru.finex.core.math.bvh;

import it.unimi.dsi.fastutil.ints.IntList;
import java.util.Collection;
import ru.finex.core.math.bv.BoundingVolume;
import ru.finex.core.math.vector.FloatMathVector;

/**
 * @author oracle
 */
public interface BvhTree<VectorType extends FloatMathVector<VectorType>, BoundingVolumeType
    extends BoundingVolume<VectorType>> {

    void build(Collection<BvhElement<VectorType, BoundingVolumeType>> elements);

    void insert(BvhElement<VectorType, BoundingVolumeType> element);

    IntList query(BoundingVolume<?> boundingVolume);

    IntList queryCircle(VectorType point, float radius);

    IntList queryBoundingVolume(BoundingVolumeType boundingVolume);

    void clear();

}

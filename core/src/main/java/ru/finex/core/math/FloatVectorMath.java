package ru.finex.core.math;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorShuffle;
import lombok.experimental.UtilityClass;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class FloatVectorMath {

    /**
     * Calculate cross product between v1 and v2 float vectors (128bit).
     * @param v1 first vector
     * @param v2 second vector
     * @return result vector
     */
    public static FloatVector cross128(FloatVector v1, FloatVector v2) {
        /*
            __m128 xzy = _mm_shuffle_ps(v1, v1, _MM_SHUFFLE(0, 2, 1, 3));
            __m128 yxz = _mm_shuffle_ps(v1, v1, _MM_SHUFFLE(1, 0, 2, 3));
            __m128 otherXzy = _mm_shuffle_ps(v2, v2, _MM_SHUFFLE(0, 2, 1, 3));
            __m128 otherYxz = _mm_shuffle_ps(v2, v2, _MM_SHUFFLE(1, 0, 2, 3));
            return _mm_sub_ps(
                _mm_mul_ps(xzy, otherYxz),
                _mm_mul_ps(yxz, otherXzy)
            );
         */

        var xzy = (FloatVector) v1.rearrange(VectorShuffle.fromValues(FloatVector.SPECIES_128, 0, 2, 1, 3));
        var yxz = (FloatVector) v1.rearrange(VectorShuffle.fromValues(FloatVector.SPECIES_128, 1, 0, 2, 3));

        var otherXzy = (FloatVector) v2.rearrange(VectorShuffle.fromValues(FloatVector.SPECIES_128, 0, 2, 1, 3));
        var otherYxz = (FloatVector) v2.rearrange(VectorShuffle.fromValues(FloatVector.SPECIES_128, 1, 0, 2, 3));

        return xzy.mul(otherYxz)
            .sub(yxz.mul(otherXzy));
    }

    /**
     * Calculate cross product between v1 and v2 float vectors (64bit).
     * @param v1 first vector
     * @param v2 second vector
     * @return result (one-dimensional point)
     */
    public static float cross64(FloatVector v1, FloatVector v2) {
        var temp = v1.mul(v2.rearrange(VectorShuffle.fromValues(FloatVector.SPECIES_64, 1, 0)));
        return temp.lane(0) - temp.lane(1);
    }

}

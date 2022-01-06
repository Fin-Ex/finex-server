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

    /**
     * Calculate dot product between v1 and v2 float vectors (128bit).
     * @param v1 first vector
     * @param v2 second vector
     * @return dot product
     */
    public static float dot128(FloatVector v1, FloatVector v2) {
        /*
        __m128 v1 = _mm_loadu_ps(...);
        __m128 v2 = _mm_loadu_ps(...);
        __m128 mult = _mm_mul_ps(v1, v2);
        __m128 tmp = _mm_hadd_ps(mult, mult); <--- m0nster.mind: vector api doesnt have hadd ops right now :(
        __m128 sum2 = _mm_hadd_ps(tmp, tmp);
         */

        var tmp = v1.mul(v2);
        return tmp.lane(0) + tmp.lane(1) + tmp.lane(2);
    }

    /**
     * Calculate dot product between v1 and v2 float vectors (128bit).
     * Four dimension version.
     * @param v1 first vector
     * @param v2 second vector
     * @return dot product
     */
    public static float dot128fd(FloatVector v1, FloatVector v2) {
        /*
        __m128 v1 = _mm_loadu_ps(...);
        __m128 v2 = _mm_loadu_ps(...);
        __m128 mult = _mm_mul_ps(v1, v2);
        __m128 tmp = _mm_hadd_ps(mult, mult); <--- m0nster.mind: vector api doesnt have hadd ops right now :(
        __m128 sum2 = _mm_hadd_ps(tmp, tmp);
         */

        var tmp = v1.mul(v2);
        return tmp.lane(0) + tmp.lane(1) + tmp.lane(2) + tmp.lane(3);
    }

}

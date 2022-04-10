package ru.finex.core.math;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorMask;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorShuffle;
import jdk.incubator.vector.VectorSpecies;
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
        // x = y1*z2 - z1*y2
        // y = z1*x2 - x1*z2
        // z = x1*y2 - y1*x1

        float[] first = v1.toArray(); // FIXME m0nster.mind: array allocation
        float[] second = v2.toArray(); // FIXME m0nster.mind: array allocation

        return rearrange128(first, shuffle128(1, 2, 0, 3))
            .mul(rearrange128(second, shuffle128(2, 0, 1, 3)))
            .sub(rearrange128(first, shuffle128(2, 0, 1, 3))
                .mul(rearrange128(second, shuffle128(1, 2, 0, 3)))
            );
    }

    /**
     * Calculate cross product between v1 and v2 float vectors (128bit).
     * Four dimension version.
     * @param v1 first vector
     * @param v2 second vector
     * @return result vector
     */
    public static FloatVector cross128fd(FloatVector v1, FloatVector v2) {
        // x = w1*x2 + x1*w2 + y1*z2 - z1*y2
        // y = w1*y2 + y1*w2 + z1*x2 - x1*z2
        // z = w1*z2 + z1*w2 + x1*y2 - y1*x2
        // w = w1*w2 - x1*x2 - y1*y2 - z1*z2

        float[] first = v1.toArray(); // FIXME m0nster.mind: array allocation
        float[] second = v2.toArray(); // FIXME m0nster.mind: array allocation

        var tmp1 = rearrange128(first, shuffle128(3, 3, 3, 3))
            .mul(v2);

        var tmp2 = rearrange128(first, shuffle128(0, 1, 2, 0))
            .mul(rearrange128(second, shuffle128(3, 3, 3, 0)))
            .lanewise(VectorOperators.NEG, mask128(false, false, false, true));

        var tmp3 = rearrange128(first, shuffle128(1, 2, 0, 1))
            .mul(rearrange128(second, shuffle128(2, 0, 1, 1)))
            .lanewise(VectorOperators.NEG, mask128(false, false, false, true));

        var tmp4 = rearrange128(first, shuffle128(2, 0, 1, 2))
            .mul(rearrange128(second, shuffle128(1, 2, 0, 2)));

        return tmp1.add(tmp2).add(tmp3).sub(tmp4);
    }

    /**
     * Calculate cross product between v1 and v2 float vectors (64bit).
     * This method rearrange v2 values!
     * @param v1 first vector
     * @param v2 second vector
     * @return result (one-dimensional point)
     */
    public static float cross64(FloatVector v1, FloatVector v2) {
        var temp = v1.mul(v2.rearrange(shuffle128(1, 0)));
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

    /**
     * Normalize 3D vector (128bit) if needed.
     *
     * @param vector vector
     * @return normalized vector
     */
    public static FloatVector normalize128(FloatVector vector) {
        FloatVector result = vector;
        var sq = vector.mul(vector);
        float length = sq.lane(0) + sq.lane(1) + sq.lane(2);

        if (length > 1.0f || length == 0f) {
            length = 1.0F / ExtMath.sqrt(length);
            result = vector.mul(length);
        }

        return result;
    }

    /**
     * Rearrange array.
     * @param array array
     * @param species species
     * @param shuffle rearrange type
     * @return float vector
     */
    public static FloatVector rearrange(float[] array, VectorSpecies<Float> species, VectorShuffle<Float> shuffle) {
        return FloatVector.fromArray(species, array, 0)
            .rearrange(shuffle);
    }

    /**
     * Rearrange 128bit array.
     * @param array array
     * @param shuffle rearrange type
     * @return float vector
     */
    public static FloatVector rearrange128(float[] array, VectorShuffle<Float> shuffle) {
        return FloatVector.fromArray(FloatVector.SPECIES_128, array, 0)
            .rearrange(shuffle);
    }

    /**
     * Creates a shuffle for a 128bit species from a series of source indexes.
     * @param indexes the source indexes which the shuffle will draw from
     * @return a shuffle where each lane's source index is set to the given int value, partially wrapped if exceptional
     * @see VectorShuffle#fromValues(VectorSpecies, int...)
     */
    public static VectorShuffle<Float> shuffle128(int... indexes) {
        return VectorShuffle.fromValues(FloatVector.SPECIES_128, indexes);
    }

    /**
     * Returns a mask where each lane is set or unset according to given boolean values.
     * For each mask lane, where N is the mask lane index, if the given boolean value at index N is true then the mask lane at index N is set,
     *  otherwise it is unset.
     * The given species (128bit) must have a number of lanes that is compatible with the given array.
     * @param bits the given boolean values
     * @return a mask where each lane is set or unset according to the given boolean value
     * @see VectorMask#fromValues(VectorSpecies, boolean...)
     */
    public static VectorMask<Float> mask128(boolean... bits) {
        return VectorMask.fromValues(FloatVector.SPECIES_128, bits);
    }

    /**
     * Loads a vector from an array of type float[] starting at an offset. For each vector lane, where N is the vector lane index,
     *  the array element at index offset + N is placed into the resulting vector at lane index N.
     * @param array the array
     * @return the vector loaded from an array
     * @see FloatVector#fromArray(VectorSpecies, float[], int)
     */
    public static FloatVector floatVector128(float[] array) {
        return FloatVector.fromArray(FloatVector.SPECIES_128, array, 0);
    }

}

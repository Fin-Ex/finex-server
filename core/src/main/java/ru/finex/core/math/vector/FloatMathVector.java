package ru.finex.core.math.vector;

import jdk.incubator.vector.FloatVector;

/**
 * Base interface for float math vectors implementation.
 * @param <SELF> real vector type.
 *
 * @author m0nster.mind
 * @author oracle
 */
public interface FloatMathVector<SELF extends FloatMathVector<SELF>> extends MathVector {

    /**
     * Get array representation of this vector.
     * Any changes in array reflected to this vector.
     *
     * @return vector components.
     */
    float[] getComponents();

    /**
     * Get copied array representation of this vector.
     *
     * @return vector components.
     */
    float[] getComponentsCopy();

    /**
     * Save {@code 32 bits * N} (N - float components number) from float vector as vector components.
     * <ul>
     *  <li>1-dimensional float vector: 32 bits</li>
     *  <li>2-dimensional float vector: 64 bits</li>
     *  <li>3-dimensional float vector: 128 bits</li>
     *  <li>4-dimensional float vector: 128 bits</li>
     * </ul>
     * @param floatVector float vector.
     * @return this.
     */
    SELF set(FloatVector floatVector);

    /**
     * Set components from the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    SELF set(SELF vector);

    /**
     * Add the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    SELF addLocal(SELF vector);

    /**
     * Adds the vector from this vector and store the result to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    SELF add(SELF vector, SELF result);

    /**
     * Adds the vector from this vector.
     *
     * @param vector the vector.
     * @return float vector (same bit depth).
     */
    FloatVector add(SELF vector);

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return this changed vector.
     */
    SELF subtractLocal(SELF vector);

    /**
     * Subtract this vector by the vector and store it to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    SELF subtract(SELF vector, SELF result);

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return float vector (same bit depth).
     */
    FloatVector subtract(SELF vector);

    /**
     * Multiply this vector by the scalar.
     *
     * @param scalar the scalar.
     * @return this vector.
     */
    SELF multLocal(float scalar);

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    SELF multLocal(SELF vector);

    /**
     * Multiply this vector by the vector and store result to result vector.
     *
     * @param vector the vector.
     * @param result the result vector.
     * @return result vector.
     */
    SELF mult(SELF vector, SELF result);

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return float vector (same bit depth).
     */
    FloatVector mult(SELF vector);

    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return this changed vector.
     */
    SELF divideLocal(SELF vector);

    /**
     * Divide this vector by the vector and store result to result vector.
     *
     * @param vector the divider vector.
     * @param result result vector
     * @return result vector.
     */
    SELF divide(SELF vector, SELF result);

    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return float vector (same bit depth).
     */
    FloatVector divide(SELF vector);

    /**
     * Calculate perpendicular vector of this and store to this vector.
     *
     * @return this vector.
     */
    SELF perpendicularLocal();

    /**
     * Calculate perpendicular vector of this and store to result vector.
     *
     * @param result the result vector.
     * @return result vector.
     */
    SELF perpendicular(SELF result);

    /**
     * Calculate dot to the vector.
     *
     * @param vector the vector.
     * @return the dot product.
     */
    float dot(SELF vector);

    /**
     * Calculate distance to the vector.
     *
     * @param vector the vector.
     * @return the distance.
     */
    float distance(SELF vector);

    /**
     * Calculate squared distance to the vector.
     *
     * @param vector the vector.
     * @return the squared distance.
     */
    float distanceSquared(SELF vector);

    /**
     * Return true if all components are zero.
     *
     * @return true if all components are zero.
     */
    boolean isZero();

    /**
     * Invert this vector to get a negative vector.
     *
     * @return this vector.
     */
    SELF negateLocal();

    /**
     * Invert this vector and store to result vector.
     *
     * @param result the result vector.
     * @return result changed vector.
     */
    SELF negate(SELF result);

    /**
     * Normalize this vector.
     *
     * @return this vector.
     */
    SELF normalizeLocal();

    /**
     * Normalize this vector and store to result vector.
     *
     * @param result the result vector.
     * @return the new normalized vector.
     */
    SELF normalize(SELF result);

    /**
     * Return vector's length (magnitude).
     *
     * @return the vector's length.
     */
    float length();

    /**
     * Return vector's squared length (magnitude).
     *
     * @return the vector's squared length.
     */
    float sqrLength();

    /**
     * Move this vector to a new point by specified direction.
     *
     * @param direction move direction.
     * @param distance move distance.
     * @return this vector.
     */
    SELF moveToDirection(SELF direction, float distance);

    /**
     * Move this vector to destination vector.
     * If distance argument is greater or equal to real distance between this vector and
     * destination vector then coordinates will be set to equal destination.
     *
     * @param destination destination vector.
     * @param distance move distance.
     * @return this vector with new position.
     */
    SELF moveToPoint(SELF destination, float distance);

    /**
     * Linear time-based interpolation stored to this vector.
     *
     * @param min the minimal vector.
     * @param max the maximal vector.
     * @param time the time.
     * @return this vector.
     */
    SELF lerp(SELF min, SELF max, float time);

    /**
     * Return angle in radians between this vector and other vector.
     *
     * @param vector another vector.
     * @return angle in radians.
     */
    float angle(SELF vector);

}

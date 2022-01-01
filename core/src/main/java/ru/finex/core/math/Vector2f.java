package ru.finex.core.math;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.isFinite;

/**
 * Implementation of float vector in 2D space (two coordinates).
 *
 * @author JavaSaBr
 * @author m0nster.mind
 */
public class Vector2f implements Cloneable {

    public static final Vector2f ZERO = new Vector2f(0, 0);
    public static final Vector2f NAN = new Vector2f(Float.NaN, Float.NaN);

    public static final Vector2f UNIT_X = new Vector2f(1, 0);
    public static final Vector2f UNIT_Y = new Vector2f(0, 1);
    public static final Vector2f UNIT_XYZ = new Vector2f(1, 1);

    public static final Vector2f UNIT_X_NEGATIVE = new Vector2f(-1, 0);
    public static final Vector2f UNIT_Y_NEGATIVE = new Vector2f(0, -1);
    public static final Vector2f UNIT_XYZ_NEGATIVE = new Vector2f(-1, -1);

    public static final Vector2f POSITIVE_INFINITY =
        new Vector2f(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

    public static final Vector2f NEGATIVE_INFINITY =
        new Vector2f(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    private static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_64;

    private final float[] components = new float[2];
    private final float[] operation = new float[2];

    public Vector2f() {
    }

    public Vector2f(float x, float y) {
        components[0] = x;
        components[1] = y;
    }

    public Vector2f(float value) {
        this(value, value);
    }

    public Vector2f(float[] components) {
        this(components[0], components[1]);
    }

    public Vector2f(Vector2f another) {
        this(another.getX(), another.getY());
    }

    public float getX() {
        return components[0];
    }

    public float getY() {
        return components[1];
    }

    /**
     * Return true if the vector is not null and valid.
     *
     * @param vector the vector.
     * @return true if the vector is not null and valid.
     */
    public static boolean isValid(Vector2f vector) {
        return vector != null && isFinite(vector.getX()) && isFinite(vector.getY());
    }

    /**
     * Return float vector with 64 bit species.
     * @return float vector of x & y components
     */
    public FloatVector floatVector() {
        return FloatVector.fromArray(SPECIES, components, 0);
    }

    /**
     * Save 64bit (two floats) from float vector as x & y components.
     * @param floatVector float vector
     */
    public void set(FloatVector floatVector) {
        floatVector.intoArray(components, 0);
    }

    /**
     * Add coordinates to this vector.
     *
     * @param addX x axis value.
     * @param addY y axis value.
     * @return this vector
     */
    public Vector2f addLocal(float addX, float addY) {
        floatVector()
            .add(fillOperation(addX, addY))
            .intoArray(components, 0);

        return this;
    }

    /**
     * Add the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector2f addLocal(Vector2f vector) {
        floatVector()
            .add(FloatVector.fromArray(SPECIES, vector.components, 0))
            .intoArray(components, 0);

        return this;
    }

    /**
     * Calculate distance to the vector.
     *
     * @param vector the vector.
     * @return the distance.
     */
    public float distance(Vector2f vector) {
        return ExtMath.sqrt(distanceSquared(vector));
    }

    /**
     * Calculate squared distance to the coordinates.
     *
     * @param targetX the target x.
     * @param targetY the target y.
     * @return the squared distance.
     */
    public float distanceSquared(float targetX, float targetY) {
        var delta = floatVector()
            .sub(fillOperation(targetX, targetY));

        delta.mul(delta)
            .intoArray(operation, 0);

        return operation[0] + operation[1];
    }

    /**
     * Calculate squared distance to the vector.
     *
     * @param vector the vector.
     * @return the squared distance.
     */
    public float distanceSquared(Vector2f vector) {
        return distanceSquared(vector.getX(), vector.getY());
    }

    /**
     * Calculate dot to the vector.
     *
     * @param vector the vector.
     * @return the dot product.
     */
    public float dot(Vector2f vector) {
        floatVector()
            .mul(vector.floatVector())
            .intoArray(operation, 0);

        return operation[0] + operation[1];
    }

    /**
     * Set the X component.
     *
     * @param x the X component.
     * @return this vector.
     */
    public Vector2f setX(float x) {
        components[0] = x;
        return this;
    }

    /**
     * Set the Y component.
     *
     * @param y the Y component.
     * @return this vector.
     */
    public Vector2f setY(float y) {
        components[1] = y;
        return this;
    }

    @Override
    public int hashCode() {
        var prime = 31;
        var result = 1;
        result = prime * result + Float.floatToIntBits(components[0]);
        result = prime * result + Float.floatToIntBits(components[1]);
        return result;
    }

    /**
     * Return true if all components are zero.
     *
     * @return true if all components are zero.
     */
    public boolean isZero() {
        return ExtMath.isZero(x) && ExtMath.isZero(y);
    }

    /**
     * Multiply this vector by the scalar.
     *
     * @param scalar the scalar.
     * @return this vector.
     */
    public Vector2f multLocal(float scalar) {
        return multLocal(scalar, scalar);
    }

    /**
     * Multiply this vector by the scalar values.
     *
     * @param x the x scalar.
     * @param y the y scalar.
     * @return this vector.
     */
    public Vector2f multLocal(float x, float y) {
        floatVector()
            .mul(fillOperation(x, y))
            .intoArray(components, 0);

        return this;
    }

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector2f multLocal(Vector2f vector) {
        floatVector()
            .mul(FloatVector.fromArray(SPECIES, vector.components, 0))
            .intoArray(components, 0);

        return this;
    }

    /**
     * Create a new vector as negative version of this vector.
     *
     * @return the new negative vector.
     */
    public Vector2f negate() {
        return new Vector2f(-getX(), -getY());
    }

    /**
     * Invert this vector to get a negative vector.
     *
     * @return this vector.
     */
    public Vector2f negateLocal() {
        floatVector()
            .neg()
            .intoArray(components, 0);

        return this;
    }

    /**
     * Create a normalized vector from this vector.
     *
     * @return the new normalized vector.
     */
    public Vector2f normalize() {
        float length = x * x + y * y;

        if (length != 1F && length != 0F) {
            length = 1.0F / ExtMath.sqrt(length);
            return new Vector2f(x * length, y * length);
        }

        return new Vector2f(x, y);
    }

    /**
     * Normalize this vector.
     *
     * @return this vector.
     */
    public Vector2f normalizeLocal() {
        var sqr = floatVector().mul(floatVector());
        sqr.intoArray(operation, 0);

        float length = operation[0] + operation[1];

        if (length != 1f && length != 0f) {
            length = 1.0f / ExtMath.sqrt(length);

            floatVector().mul(length)
                .intoArray(components, 0);
        }

        return this;
    }

    /**
     * Set components from the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector2f set(Vector2f vector) {
        return set(vector.getX(), vector.getY());
    }

    /**
     * Set the components to this vector.
     *
     * @param x x component.
     * @param y y component.
     * @return this vector.
     */
    public Vector2f set(float x, float y) {
        components[0] = x;
        components[1] = y;
        return this;
    }

    /**
     * Subtract this vector by the vector and store it to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    public Vector2f subtract(Vector2f vector, Vector2f result) {
        floatVector()
            .sub(vector.floatVector())
            .intoArray(result.components, 0);

        return result;
    }

    /**
     * Subtract the components from this vector.
     *
     * @param subX the sub x.
     * @param subY the sub y.
     * @return this changed vector.
     */
    public Vector2f subtractLocal(float subX, float subY) {
        floatVector()
            .sub(fillOperation(subX, subY))
            .intoArray(components, 0);

        return this;
    }

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return this changed vector.
     */
    public Vector2f subtractLocal(Vector2f vector) {
        floatVector()
            .sub(vector.floatVector())
            .intoArray(components, 0);

        return this;
    }

    /**
     * Return vector's length (magnitude).
     *
     * @return the vector's length.
     */
    public float length() {
        return ExtMath.sqrt(x * x + y * y);
    }

    /**
     * Return vector's squared length (magnitude).
     *
     * @return the vector's squared length.
     */
    public float sqrLength() {
        return x * x + y * y;
    }

    /**
     * Divide this vector by the components.
     *
     * @param x the divider x.
     * @param y the divider y.
     * @return this changed vector.
     */
    public Vector2f divideLocal(float x, float y) {
        floatVector()
            .div(fillOperation(x, y))
            .intoArray(components, 0);

        return this;
    }

    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return this changed vector.
     */
    public Vector2f divideLocal(Vector2f vector) {
        floatVector()
            .div(vector.floatVector())
            .intoArray(components, 0);

        return this;
    }

    /**
     * Divide this vector by the scalar.
     *
     * @param scalar the divider scalar.
     * @return this changed vector.
     */
    public Vector2f divideLocal(float scalar) {
        floatVector()
            .div(scalar)
            .intoArray(components, 0);

        return this;
    }

    /**
     * Linear time-based interpolation stored to this vector.
     *
     * @param min the minimal vector
     * @param max the maximal vector
     * @param t the time
     * @return this vector.
     */
    public Vector2f lerp(Vector2f min, Vector2f max, float t) {
        t = ExtMath.clamp(t);

        var maxVector = max.floatVector();
        var minVector = min.floatVector();

        minVector.add(
            maxVector.sub(minVector)
                .mul(t)
        ).intoArray(components, 0);

        return this;
    }

    /**
     * Return angle in radians between this vector and other vector.
     *
     * @param other other vector
     * @return angle in radians
     */
    public float angle(Vector2f other) {
        float length = ExtMath.sqrt(sqrLength() * other.sqrLength());
        if (length < ExtMath.EPSILON) {
            return 0;
        }

        return ExtMath.acos(ExtMath.clamp(dot(other) / length, -1f, 1f));
    }

    @Override
    public Vector2f clone() {
        try {
            return (Vector2f) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check vectors to equals with epsilon.
     *
     * @param vector vector
     * @param epsilon epsilon
     * @return true if vectors equals
     */
    public boolean equals(Vector3f vector, float epsilon) {
        return Math.abs(x - vector.getX()) < epsilon && Math.abs(y - vector.getY()) < epsilon;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (getClass() != obj.getClass()) {
            return false;
        }

        var other = (Vector2f) obj;

        if (floatToIntBits(getX()) != floatToIntBits(other.getX())) {
            return false;
        } else if (floatToIntBits(getY()) != floatToIntBits(other.getY())) {
            return false;
        }

        return true;
    }

    /**
     * Return true if these vectors are equal with the epsilon.
     *
     * @param vector  the vector.
     * @param epsilon the epsilon.
     * @return true if these vectors are equal with the epsilon.
     */
    public boolean equals(Vector2f vector, float epsilon) {
        return Math.abs(getX() - vector.getX()) < epsilon &&
            Math.abs(getY() - vector.getY()) < epsilon;
    }

    @Override
    public String toString() {
        return "Vector2f(x: " + getX() + ", y: " + getY() + ')';
    }

    private FloatVector fillOperation(float x, float y) {
        operation[0] = x;
        operation[1] = y;
        return FloatVector.fromArray(SPECIES, operation, 0);
    }
}
package ru.finex.core.math.vector;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorShuffle;
import jdk.incubator.vector.VectorSpecies;
import ru.finex.core.math.ExtMath;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.isFinite;

/**
 * Implementation of float vector in 2D space (two coordinates).
 *
 * @author JavaSaBr
 * @author m0nster.mind
 */
public class Vector2f implements MathVector, Cloneable {

    public static final Vector2f ZERO = new Vector2f(0, 0);
    public static final Vector2f NAN = new Vector2f(Float.NaN, Float.NaN);

    public static final Vector2f UNIT_X = new Vector2f(1, 0);
    public static final Vector2f UNIT_Y = new Vector2f(0, 1);
    public static final Vector2f UNIT_XY = new Vector2f(1, 1);

    public static final Vector2f UNIT_X_NEGATIVE = new Vector2f(-1, 0);
    public static final Vector2f UNIT_Y_NEGATIVE = new Vector2f(0, -1);
    public static final Vector2f UNIT_XY_NEGATIVE = new Vector2f(-1, -1);

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
        this(another.components);
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

    public float getX() {
        return components[0];
    }

    public float getY() {
        return components[1];
    }

    /**
     * Return float vector with 64 bit species.
     * @return float vector of x & y components
     */
    public FloatVector floatVector() {
        return FloatVector.fromArray(SPECIES, components, 0);
    }

    /**
     * Return float vector with specified species.
     * @param species line width
     * @return float vector
     */
    public FloatVector floatVector(VectorSpecies<Float> species) {
        return FloatVector.fromArray(species, components, 0);
    }

    /**
     * Save 64bit (two floats) from float vector as x & y components.
     * @param floatVector float vector
     */
    public void set(FloatVector floatVector) {
        floatVector.intoArray(components, 0);
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

    /**
     * Add coordinates to this vector.
     *
     * @param x x axis value.
     * @param y y axis value.
     * @return this vector
     */
    public Vector2f addLocal(float x, float y) {
        add(floatVector(), fillOperation(x, y), components);
        return this;
    }

    /**
     * Add the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector2f addLocal(Vector2f vector) {
        return add(vector, this);
    }

    /**
     * Adds the vector from this vector and store the result to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    public Vector2f add(Vector2f vector, Vector2f result) {
        add(floatVector(), vector.floatVector(), result.components);
        return result;
    }

    private static void add(FloatVector v1, FloatVector v2, float[] components) {
        v1.add(v2).intoArray(components, 0);
    }

    /**
     * Subtract the components from this vector.
     *
     * @param x the sub x.
     * @param y the sub y.
     * @return this changed vector.
     */
    public Vector2f subtractLocal(float x, float y) {
        subtract(floatVector(), fillOperation(x, y), components);
        return this;
    }

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return this changed vector.
     */
    public Vector2f subtractLocal(Vector2f vector) {
        return subtract(vector, this);
    }

    /**
     * Subtract this vector by the vector and store it to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    public Vector2f subtract(Vector2f vector, Vector2f result) {
        subtract(floatVector(), vector.floatVector(), result.components);
        return result;
    }

    private static void subtract(FloatVector v1, FloatVector v2, float[] components) {
        v1.sub(v2).intoArray(components, 0);
    }

    /**
     * Multiply this vector by the scalar.
     *
     * @param scalar the scalar.
     * @return this vector.
     */
    public Vector2f multLocal(float scalar) {
        floatVector().mul(scalar).intoArray(components, 0);
        return this;
    }

    /**
     * Multiply this vector by the scalar values.
     *
     * @param x the x scalar.
     * @param y the y scalar.
     * @return this vector.
     */
    public Vector2f multLocal(float x, float y) {
        mult(floatVector(), fillOperation(x, y), components);
        return this;
    }

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector2f multLocal(Vector2f vector) {
        return mult(vector, this);
    }

    /**
     * Multiply this vector by the vector and store result to result vector.
     *
     * @param vector the vector.
     * @param result the result vector
     * @return result vector.
     */
    public Vector2f mult(Vector2f vector, Vector2f result) {
        mult(floatVector(), vector.floatVector(), result.components);
        return this;
    }

    private static void mult(FloatVector v1, FloatVector v2, float[] components) {
        v1.mul(v2).intoArray(components, 0);
    }

    /**
     * Divide this vector by the scalar.
     *
     * @param scalar the divider scalar.
     * @return this changed vector.
     */
    public Vector2f divideLocal(float scalar) {
        floatVector().div(scalar).intoArray(components, 0);
        return this;
    }

    /**
     * Divide this vector by the components.
     *
     * @param x the divider x.
     * @param y the divider y.
     * @return this changed vector.
     */
    public Vector2f divideLocal(float x, float y) {
        divide(floatVector(), fillOperation(x, y), components);
        return this;
    }

    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return this changed vector.
     */
    public Vector2f divideLocal(Vector2f vector) {
        return divide(vector, this);
    }

    /**
     * Divide this vector by the vector and store result to result vector.
     *
     * @param vector the divider vector.
     * @param result result vector
     * @return result vector.
     */
    public Vector2f divide(Vector2f vector, Vector2f result) {
        divide(floatVector(), vector.floatVector(), result.components);
        return this;
    }

    private static void divide(FloatVector v1, FloatVector v2, float[] components) {
        v1.div(v2).intoArray(components, 0);
    }

    /**
     * Calculate a cross product (z-axis is zero) between this vector and the coordinates.
     *
     * @param x the other x.
     * @param y the other y.
     * @return cross product.
     */
    public float cross(float x, float y) {
        return cross(floatVector(), fillOperation(x, y), operation);
    }

    /**
     * Calculate a cross product (z-axis is zero) between this vector and the vector.
     *
     * @param vector the vector.
     * @return cross product.
     */
    public float cross(Vector2f vector) {
        var vectorLine = vector.floatVector();
        floatVector().mul(
            vectorLine.rearrange(VectorShuffle.fromValues(SPECIES, 1, 0))
        ).intoArray(operation, 0);

        return operation[0] - operation[1];
    }

    private static float cross(FloatVector v1, FloatVector v2, float[] components) {
        v1.mul(v2.rearrange(VectorShuffle.fromValues(SPECIES, 1, 0)))
            .intoArray(components, 0);

        return components[0] - components[1];
    }

    /**
     * Calculate perpendicular vector of this and store to this vector.
     * @return this vector.
     */
    public Vector2f perpendicularLocal() {
        return perpendicular(this);
    }

    /**
     * Calculate perpendicular vector of this and store to result vector.
     * @param result the result vector.
     * @return result vector.
     */
    public Vector2f perpendicular(Vector2f result) {
        result.set(getY(), -getX());
        return result;
    }

    /**
     * Calculate dot to the vector.
     *
     * @param vector the vector.
     * @return the dot product.
     */
    public float dot(Vector2f vector) {
        floatVector().mul(vector.floatVector())
            .intoArray(operation, 0);

        return operation[0] + operation[1];
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
     * Return true if all components are zero.
     *
     * @return true if all components are zero.
     */
    public boolean isZero() {
        return ExtMath.isZero(getX()) && ExtMath.isZero(getY());
    }

    /**
     * Invert this vector to get a negative vector.
     *
     * @return this vector.
     */
    public Vector2f negateLocal() {
        return negate(this);
    }

    /**
     * Invert this vector and store to result vector.
     *
     * @param result the result vector.
     * @return result changed vector.
     */
    public Vector2f negate(Vector2f result) {
        floatVector().neg()
            .intoArray(result.components, 0);

        return result;
    }

    /**
     * Normalize this vector.
     *
     * @return this vector.
     */
    public Vector2f normalizeLocal() {
        return normalize(this);
    }

    /**
     * Normalize this vector and store to result vector.
     *
     * @param result the result vector.
     * @return the new normalized vector.
     */
    public Vector2f normalize(Vector2f result) {
        float length = sqrLength();

        if (length != 1F && length != 0F) {
            length = 1.0F / ExtMath.sqrt(length);
            result.set(getX() * length, getY() * length);
        } else {
            result.set(this);
        }

        return result;
    }

    /**
     * Return vector's length (magnitude).
     *
     * @return the vector's length.
     */
    public float length() {
        return ExtMath.sqrt(sqrLength());
    }

    /**
     * Return vector's squared length (magnitude).
     *
     * @return the vector's squared length.
     */
    public float sqrLength() {
        return components[0] * components[0] +
            components[1] * components[1];
    }

    /**
     * Move this vector to a new point by specified direction.
     *
     * @param direction move direction.
     * @param distance  move distance.
     * @return this vector.
     */
    public Vector2f moveToDirection(Vector2f direction, float distance) {
        direction.floatVector()
            .fma(fillOperation(distance, distance), floatVector())
            .intoArray(components, 0);

        return this;
    }

    /**
     * Move this vector to destination vector.
     * If distance argument is greater or equal to real distance between this vector and
     * destination vector then coordinates will be set to equal destination.
     *
     * @param destination destination vector
     * @param distance    move distance
     * @return this vector with new position
     */
    public Vector2f moveToPoint(Vector2f destination, float distance) {
        var direction = destination.floatVector()
            .sub(floatVector());

        direction.mul(direction)
            .intoArray(operation, 0);

        float length = (float) Math.sqrt(operation[0] + operation[1]); // XXX double?
        if (length <= distance || length < ExtMath.EPSILON) {
            set(destination);
            return this;
        }

        direction.div(length) // normalize vector
            .fma(fillOperation(length, length), floatVector()) // Vn * Dn + Pn
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
    @SuppressWarnings("checkstyle:ParameterAssignment")
    public Vector2f lerp(Vector2f min, Vector2f max, float t) {
        t = ExtMath.clamp(t);

        var minLines = min.floatVector();
        var delta = max.floatVector()
            .sub(minLines);

        minLines.fma(fillOperation(t, t), delta)
            .intoArray(components, 0);

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
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(components[0]);
        result = prime * result + Float.floatToIntBits(components[1]);
        return result;
    }

    @Override
    public Vector2f clone() {
        try {
            return (Vector2f) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
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
        }

        return floatToIntBits(getY()) == floatToIntBits(other.getY());
    }

    /**
     * Check vectors to equals with epsilon.
     *
     * @param vector vector
     * @param epsilon epsilon
     * @return true if vectors equals
     */
    public boolean equals(Vector3f vector, float epsilon) {
        return floatVector().sub(vector.floatVector(SPECIES))
            .abs()
            .lt(epsilon)
            .allTrue();
    }

    /**
     * Return true if these vectors are equal with the epsilon.
     *
     * @param vector  the vector.
     * @param epsilon the epsilon.
     * @return true if these vectors are equal with the epsilon.
     */
    public boolean equals(Vector2f vector, float epsilon) {
        return floatVector().sub(vector.floatVector())
            .abs()
            .lt(epsilon)
            .allTrue();
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
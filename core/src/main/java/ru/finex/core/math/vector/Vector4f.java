package ru.finex.core.math.vector;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorSpecies;
import manifold.ext.rt.api.ComparableUsing;
import ru.finex.core.math.ExtMath;
import ru.finex.core.math.FloatVectorMath;

import java.util.Arrays;

import static java.lang.Float.floatToIntBits;
import static java.lang.Float.isFinite;

/**
 * The implementation of vector with 4 float values.
 *
 * @author m0nster.mind
 */
public class Vector4f implements MathVector, Cloneable, ComparableUsing<Vector4f> {

    public static final Vector4f ZERO = new Vector4f(0, 0, 0, 0);
    public static final Vector4f NAN = new Vector4f(Float.NaN, Float.NaN, Float.NaN, Float.NaN);

    public static final Vector4f UNIT_X = new Vector4f(1, 0, 0, 0);
    public static final Vector4f UNIT_Y = new Vector4f(0, 1, 0, 0);
    public static final Vector4f UNIT_Z = new Vector4f(0, 0, 1, 0);
    public static final Vector4f UNIT_W = new Vector4f(0, 0, 0, 1);
    public static final Vector4f UNIT_XYZ = new Vector4f(1, 1, 1, 0);
    public static final Vector4f UNIT_XYZW = new Vector4f(1, 1, 1, 1);

    public static final Vector4f UNIT_X_NEGATIVE = new Vector4f(-1, 0, 0, 0);
    public static final Vector4f UNIT_Y_NEGATIVE = new Vector4f(0, -1, 0, 0);
    public static final Vector4f UNIT_Z_NEGATIVE = new Vector4f(0, 0, -1, 0);
    public static final Vector4f UNIT_W_NEGATIVE = new Vector4f(0, 0, 0, -1);
    public static final Vector4f UNIT_XYZW_NEGATIVE = new Vector4f(-1, -1, -1, -1);

    public static final Vector4f POSITIVE_INFINITY = new Vector4f(Float.POSITIVE_INFINITY,
        Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.NEGATIVE_INFINITY);

    public static final Vector4f NEGATIVE_INFINITY = new Vector4f(Float.NEGATIVE_INFINITY,
        Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    public static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_128;

    protected final float[] components = new float[4];
    protected final float[] operation = new float[4];

    public Vector4f() {
        super();
    }

    public Vector4f(float value) {
        Arrays.fill(components, 0, 3, value);
    }

    public Vector4f(float x, float y, float z, float w) {
        components[0] = x;
        components[1] = y;
        components[2] = z;
        components[3] = w;
    }

    public Vector4f(float[] components) {
        this(components[0], components[1], components[2], components[3]);
    }

    public Vector4f(Vector4f another) {
        this(another.components);
    }

    /**
     * Return true if the vector is not null and valid.
     *
     * @param vector the vector.
     * @return true if the vector is not null and valid.
     */
    public static boolean isValid(Vector4f vector) {
        return vector != null &&
            isFinite(vector.getX()) &&
            isFinite(vector.getY()) &&
            isFinite(vector.getZ()) &&
            isFinite(vector.getW());
    }

    /**
     * Get X component.
     * @return x
     */
    public float getX() {
        return components[0];
    }

    /**
     * Get Y component.
     * @return y
     */
    public float getY() {
        return components[1];
    }

    /**
     * Get Z component.
     * @return z
     */
    public float getZ() {
        return components[2];
    }

    /**
     * Get W component.
     * @return w
     */
    public float getW() {
        return components[3];
    }

    /**
     * Get array representation of this vector.
     * Any changes in array reflected to this vector.
     *
     * @return vector components
     */
    public float[] getComponents() {
        return components;
    }

    /**
     * Get array representation of this vector.
     *
     * @return vector components
     */
    public float[] getComponentsCopy() {
        return Arrays.copyOf(components, 4);
    }

    /**
     * Return float vector with 128 bit species.
     * @return float vector of x, y, z, w components
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
     * Save 128bit (three floats) from float vector as x, y, z components.
     * @param floatVector float vector
     * @return this
     */
    public Vector4f set(FloatVector floatVector) {
        floatVector.intoArray(components, 0);
        return this;
    }

    /**
     * Set components from the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector4f set(Vector4f vector) {
        return set(vector.getX(), vector.getY(), vector.getZ(), vector.getW());
    }

    /**
     * Set the components to this vector.
     *
     * @param x x component.
     * @param y y component.
     * @param z z component.
     * @param w w component
     * @return this vector.
     */
    public Vector4f set(float x, float y, float z, float w) {
        components[0] = x;
        components[1] = y;
        components[2] = z;
        components[3] = w;
        return this;
    }

    /**
     * Set the X component.
     *
     * @param x the X component.
     * @return this vector.
     */
    public Vector4f setX(float x) {
        components[0] = x;
        return this;
    }

    /**
     * Set the Y component.
     *
     * @param y the Y component.
     * @return this vector.
     */
    public Vector4f setY(float y) {
        components[1] = y;
        return this;
    }

    /**
     * Set the Z component.
     *
     * @param z the Z component.
     * @return this vector.
     */
    public Vector4f setZ(float z) {
        components[2] = z;
        return this;
    }

    /**
     * Set the W component.
     *
     * @param w the W component.
     * @return this vector.
     */
    public Vector4f setW(float w) {
        components[3] = w;
        return this;
    }

    /**
     * Add the values to this vector.
     *
     * @param x x axis value.
     * @param y y axis value.
     * @param z z axis value.
     * @param w w axis value.
     * @return this vector.
     */
    public Vector4f addLocal(float x, float y, float z, float w) {
        add(floatVector(), fillOperation(x, y, z, w), components);
        return this;
    }

    /**
     * Add the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector4f addLocal(Vector4f vector) {
        return add(vector, this);
    }

    /**
     * Adds the vector from this vector and store the result to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    public Vector4f add(Vector4f vector, Vector4f result) {
        add(floatVector(), vector.floatVector(), result.components);
        return result;
    }

    /**
     * Adds the vector from this vector.
     *
     * @param vector the vector.
     * @return float vector (128bit)
     */
    public FloatVector add(Vector4f vector) {
        return floatVector().add(vector.floatVector());
    }

    private static void add(FloatVector v1, FloatVector v2, float[] components) {
        v1.add(v2).intoArray(components, 0);
    }

    /**
     * Subtract the components from this vector.
     *
     * @param x the sub x.
     * @param y the sub y.
     * @param z the sub z.
     * @param w the sub w
     * @return this vector.
     */
    public Vector4f subtractLocal(float x, float y, float z, float w) {
        subtract(floatVector(), fillOperation(x, y, z, w), components);
        return this;
    }

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector4f subtractLocal(Vector4f vector) {
        return subtract(vector, this);
    }

    /**
     * Subtract the vector from this vector and store the result to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    public Vector4f subtract(Vector4f vector, Vector4f result) {
        subtract(floatVector(), vector.floatVector(), result.components);
        return result;
    }

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return float vector (128bit)
     */
    public FloatVector subtract(Vector4f vector) {
        return floatVector().sub(vector.floatVector());
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
    public Vector4f multLocal(float scalar) {
        floatVector().mul(scalar)
            .intoArray(components, 0);

        return this;
    }

    /**
     * Multiply this vector by the scalar values.
     *
     * @param x the x scalar.
     * @param y the y scalar.
     * @param z the z scalar.
     * @param w the w scalar
     * @return this vector.
     */
    public Vector4f multLocal(float x, float y, float z, float w) {
        mult(floatVector(), fillOperation(x, y, z, w), components);
        return this;
    }

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector4f multLocal(Vector4f vector) {
        return mult(vector, this);
    }

    /**
     * Multiply this vector by the vector and store result to result vector.
     *
     * @param vector the vector.
     * @param result the result vector
     * @return result vector.
     */
    public Vector4f mult(Vector4f vector, Vector4f result) {
        mult(floatVector(), vector.floatVector(), result.components);
        return this;
    }

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return float vector (128bit)
     */
    public FloatVector mult(Vector4f vector) {
        return floatVector().mul(vector.floatVector());
    }

    private static void mult(FloatVector v1, FloatVector v2, float[] components) {
        v1.mul(v2).intoArray(components, 0);
    }

    /**
     * Divide this vector by the scalar.
     *
     * @param scalar the divider scalar.
     * @return this vector.
     */
    public Vector4f divideLocal(float scalar) {
        floatVector().div(scalar).intoArray(components, 0);
        return this;
    }

    /**
     * Divide this vector by the components.
     *
     * @param x the divider x.
     * @param y the divider y.
     * @param z the divider z.
     * @param w the divider w.
     * @return this vector.
     */
    public Vector4f divideLocal(float x, float y, float z, float w) {
        divide(floatVector(), fillOperation(x, y, z, w), components);
        return this;
    }

    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return this vector.
     */
    public Vector4f divideLocal(Vector4f vector) {
        return divide(vector, this);
    }

    /**
     * Divide this vector by the vector and store result to result vector.
     *
     * @param vector the divider vector.
     * @param result result vector
     * @return result vector.
     */
    public Vector4f divide(Vector4f vector, Vector4f result) {
        divide(floatVector(), vector.floatVector(), result.components);
        return this;
    }

    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return float vector (128bit).
     */
    public FloatVector divide(Vector4f vector) {
        return floatVector().div(vector.floatVector());
    }

    private static void divide(FloatVector v1, FloatVector v2, float[] components) {
        v1.div(v2).intoArray(components, 0);
    }

    /**
     * Calculate a cross vector between this vector and the vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector4f crossLocal(Vector4f vector) {
        return cross(vector, this);
    }

    /**
     * Calculate a cross vector between this vector and the coordinates and store the result to result vector.
     *
     * @param vector the vector.
     * @param result the result vector
     * @return result changed vector.
     */
    public Vector4f cross(Vector4f vector, Vector4f result) {
        FloatVectorMath.wedge128fd(floatVector(), vector.floatVector())
            .intoArray(result.components, 0);

        return result;
    }

    /**
     * Calculate a cross vector between this vector and the vector.
     *
     * @param vector the vector
     * @return float vector (128bit)
     */
    public FloatVector cross(Vector4f vector) {
        return FloatVectorMath.wedge128fd(floatVector(), vector.floatVector());
    }

    /**
     * Calculate dot to the vector.
     *
     * @param vector the vector.
     * @return the dot product.
     */
    public float dot(Vector4f vector) {
        return FloatVectorMath.dot128fd(floatVector(), vector.floatVector());
    }

    /**
     * Calculate distance to the vector.
     *
     * @param vector the vector.
     * @return the distance.
     */
    public float distance(Vector4f vector) {
        return ExtMath.sqrt(distanceSquared(vector));
    }

    /**
     * Calculate squared distance to the coordinates.
     *
     * @param x the target x.
     * @param y the target y.
     * @param z the target z.
     * @param w the target w.
     * @return the squared distance.
     */
    public float distanceSquared(float x, float y, float z, float w) {
        return distanceSquared(floatVector(), fillOperation(x, y, z, w), operation);
    }

    /**
     * Calculate squared distance to the vector.
     *
     * @param vector the vector.
     * @return the squared distance.
     */
    public float distanceSquared(Vector4f vector) {
        return distanceSquared(floatVector(), vector.floatVector(), operation);
    }

    private static float distanceSquared(FloatVector v1, FloatVector v2, float[] components) {
        var delta = v1.sub(v2);
        delta.mul(delta)
            .intoArray(components, 0);

        return components[0] + components[1] + components[2] + components[3];
    }

    /**
     * Invert this vector to get a negative vector.
     *
     * @return this vector.
     */
    public Vector4f negateLocal() {
        return negate(this);
    }

    /**
     * Invert this vector and store to result vector.
     *
     * @param result result vector
     * @return result changed vector.
     */
    public Vector4f negate(Vector4f result) {
        floatVector().neg()
            .intoArray(result.components, 0);

        return result;
    }

    /**
     * Normalize this vector.
     *
     * @return this vector.
     */
    public Vector4f normalizeLocal() {
        return normalize(this);
    }

    /**
     * Normalize this vector and save result to result vector.
     *
     * @param result the result vector
     * @return result changed vector
     */
    public Vector4f normalize(Vector4f result) {
        float length = sqrLength();

        if (length != 1F && length != 0F) {
            length = 1.0F / ExtMath.sqrt(length);
            result.set(getX() * length, getY() * length, getZ() * length, getW() * length);
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
            components[1] * components[1] +
            components[2] * components[2] +
            components[3] * components[3];
    }

    /**
     * Move this vector to a new point by specified direction.
     *
     * @param direction move direction.
     * @param distance  move distance.
     * @return this vector.
     */
    public Vector4f moveToDirection(Vector4f direction, float distance) {
        direction.floatVector()
            .fma(fillOperation(distance, distance, distance, distance), floatVector())
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
    public Vector4f moveToPoint(Vector4f destination, float distance) {
        var direction = destination.floatVector()
            .sub(floatVector());

        direction.mul(direction)
            .intoArray(operation, 0);

        float length = (float) Math.sqrt(operation[0] + operation[1] + operation[2] + operation[3]); // XXX double?
        if (length <= distance || length < ExtMath.EPSILON) {
            set(destination);
            return this;
        }

        direction.div(length) // normalize vector
            .fma(fillOperation(distance, distance, distance, distance), floatVector()) // Vn * Dn + Pn
            .intoArray(components, 0);

        return this;
    }

    /**
     * Linear time-based interpolation stored to this vector.
     *
     * @param min the minimal vector.
     * @param max the maximal vector.
     * @param t the time.
     * @return this vector.
     */
    @SuppressWarnings("checkstyle:ParameterAssignment")
    public Vector4f lerp(Vector4f min, Vector4f max, float t) {
        t = ExtMath.clamp(t);

        var minLines = min.floatVector();
        var delta = max.floatVector()
            .sub(minLines);

        delta.fma(fillOperation(t, t, t, t), minLines)
            .intoArray(components, 0);

        return this;
    }

    /**
     * Return angle in radians between this vector and other vector.
     *
     * @param other other vector
     * @return angle in radians
     */
    public float angle(Vector4f other) {
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
        result = prime * result + Float.floatToIntBits(components[2]);
        result = prime * result + Float.floatToIntBits(components[3]);
        return result;
    }

    @Override
    public Vector4f clone() {
        try {
            return (Vector4f) super.clone();
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

        var other = (Vector4f) obj;
        if (floatToIntBits(getX()) != floatToIntBits(other.getX())) {
            return false;
        } else if (floatToIntBits(getY()) != floatToIntBits(other.getY())) {
            return false;
        }

        return floatToIntBits(getZ()) == floatToIntBits(other.getZ());
    }

    /**
     * Return true if these vectors are equal with the epsilon.
     *
     * @param vector  the vector.
     * @param epsilon the epsilon.
     * @return true if these vectors are equal with the epsilon.
     */
    public boolean equals(Vector4f vector, float epsilon) {
        return floatVector().sub(vector.floatVector())
            .abs()
            .lt(epsilon)
            .allTrue();
    }

    /**
     * Return true if all components are zero.
     *
     * @return true if all components are zero.
     */
    public boolean isZero() {
        return this == ZERO ||
            ExtMath.isZero(getX()) &&
                ExtMath.isZero(getY()) &&
                ExtMath.isZero(getZ());
    }

    @Override
    public String toString() {
        return "Vector4f(x: " + getX() + ", y: " + getY() + ", z: " + getZ() + ", w: " + getW() + ")";
    }

    protected FloatVector fillOperation(float x, float y, float z, float w) {
        operation[0] = x;
        operation[1] = y;
        operation[2] = z;
        operation[3] = w;
        return FloatVector.fromArray(SPECIES, operation, 0);
    }

    /* Operator overriding */

    /**
     * Operator {@code +} for Vector4f.
     *
     * @param rhs right value
     * @return this
     * @see #addLocal(Vector4f)
     */
    public Vector4f plus(Vector4f rhs) {
        return addLocal(rhs);
    }

    /**
     * Operator {@code +} for float.
     *
     * @param rhs right value
     * @return this
     * @see #addLocal(float, float, float, float)
     */
    public Vector4f plus(float rhs) {
        return addLocal(rhs, rhs, rhs, rhs);
    }

    /**
     * Operator {@code -} for Vector4f.
     *
     * @param rhs right value
     * @return this
     * @see #subtractLocal(Vector4f)
     */
    public Vector4f minus(Vector4f rhs) {
        return subtractLocal(rhs);
    }

    /**
     * Operator {@code -} for float.
     *
     * @param rhs right value
     * @return this
     * @see #subtractLocal(float, float, float, float)
     */
    public Vector4f minus(float rhs) {
        return subtractLocal(rhs, rhs, rhs, rhs);
    }

    /**
     * Operator {@code *} for Vector4f.
     *
     * @param rhs right value
     * @return this
     * @see #multLocal(Vector4f)
     */
    public Vector4f times(Vector4f rhs) {
        return multLocal(rhs);
    }

    /**
     * Operator {@code *} for float.
     *
     * @param rhs right value
     * @return this
     * @see #multLocal(float)
     */
    public Vector4f times(float rhs) {
        return multLocal(rhs);
    }

    /**
     * Operator {@code /} for Vector4f.
     *
     * @param rhs right value
     * @return this
     * @see #divideLocal(Vector4f)
     */
    public Vector4f div(Vector4f rhs) {
        return divideLocal(rhs);
    }

    /**
     * Operator {@code /} for float.
     *
     * @param rhs right value
     * @return this
     * @see #divideLocal(float)
     */
    public Vector4f div(float rhs) {
        return divideLocal(rhs);
    }

    /**
     * Operator {@code %} for Vector4f.
     *
     * @param rhs right value
     * @return cross product
     * @see #cross(Vector4f)
     */
    public Vector4f rem(Vector4f rhs) {
        return crossLocal(rhs);
    }

    /**
     * Unary operator {@code -} (negate).
     *
     * @return this
     * @see #negateLocal()
     */
    public Vector4f unaryMinus() {
        return negateLocal();
    }

    @Override
    public int compareTo(Vector4f o) {
        // requires by ComparableUsing, stub comparable.
        // using only for override equals operator (== and !=)
        throw new RuntimeException("Not supported.");
    }

    @Override
    public EqualityMode equalityMode() {
        return EqualityMode.Equals;
    }
}
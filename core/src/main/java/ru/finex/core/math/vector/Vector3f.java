package ru.finex.core.math.vector;

import jdk.incubator.vector.FloatVector;
import jdk.incubator.vector.VectorOperators;
import jdk.incubator.vector.VectorSpecies;
import ru.finex.core.math.ExtMath;
import ru.finex.core.math.FloatVectorMath;
import ru.finex.core.math.Quaternion;

import java.util.Arrays;

import static ru.finex.core.math.FloatVectorMath.mask128;
import static ru.finex.core.math.FloatVectorMath.rearrange128;
import static ru.finex.core.math.FloatVectorMath.shuffle128;
import static java.lang.Float.floatToIntBits;
import static java.lang.Float.isFinite;

/**
 * The implementation of vector with 3 float values.
 *
 * @author JavaSaBr
 * @author m0nster.mind
 */
public final class Vector3f implements MathVector, Cloneable {

    public static final Vector3f ZERO = new Vector3f(0, 0, 0);
    public static final Vector3f NAN = new Vector3f(Float.NaN, Float.NaN, Float.NaN);

    public static final Vector3f UNIT_X = new Vector3f(1, 0, 0);
    public static final Vector3f UNIT_Y = new Vector3f(0, 1, 0);
    public static final Vector3f UNIT_Z = new Vector3f(0, 0, 1);
    public static final Vector3f UNIT_XYZ = new Vector3f(1, 1, 1);

    public static final Vector3f UNIT_X_NEGATIVE = new Vector3f(-1, 0, 0);
    public static final Vector3f UNIT_Y_NEGATIVE = new Vector3f(0, -1, 0);
    public static final Vector3f UNIT_Z_NEGATIVE = new Vector3f(0, 0, -1);
    public static final Vector3f UNIT_XYZ_NEGATIVE = new Vector3f(-1, -1, -1);

    public static final Vector3f POSITIVE_INFINITY = new Vector3f(Float.POSITIVE_INFINITY,
        Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY);

    public static final Vector3f NEGATIVE_INFINITY = new Vector3f(Float.NEGATIVE_INFINITY,
        Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY);

    public static final VectorSpecies<Float> SPECIES = FloatVector.SPECIES_128;

    private final float[] components = new float[4];
    private final float[] operation = new float[4];

    public Vector3f() {
        super();
    }

    public Vector3f(float value) {
        Arrays.fill(components, 0, 3, value);
    }

    public Vector3f(float x, float y, float z) {
        components[0] = x;
        components[1] = y;
        components[2] = z;
    }

    public Vector3f(float[] components) {
        this(components[0], components[1], components[2]);
    }

    public Vector3f(Vector3f another) {
        this(another.getX(), another.getY(), another.getZ());
    }

    /**
     * Return true if the vector is not null and valid.
     *
     * @param vector the vector.
     * @return true if the vector is not null and valid.
     */
    public static boolean isValid(Vector3f vector) {
        return vector != null &&
            isFinite(vector.getX()) &&
            isFinite(vector.getY()) &&
            isFinite(vector.getZ());
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
     * @return float vector of x, y, z components
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
    public Vector3f set(FloatVector floatVector) {
        floatVector.intoArray(components, 0);
        return this;
    }

    /**
     * Set components from the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector3f set(Vector3f vector) {
        return set(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Set components from four dimension vector to this vector.
     * W component of four-dimension vector has been dropped.
     *
     * @param vector four dimension vector
     * @return this vector
     */
    public Vector3f set(Vector4f vector) {
        return set(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Set the components to this vector.
     *
     * @param x x component.
     * @param y y component.
     * @param z z component.
     * @return this vector.
     */
    public Vector3f set(float x, float y, float z) {
        components[0] = x;
        components[1] = y;
        components[2] = z;
        return this;
    }

    /**
     * Set the X component.
     *
     * @param x the X component.
     * @return this vector.
     */
    public Vector3f setX(float x) {
        components[0] = x;
        return this;
    }

    /**
     * Set the Y component.
     *
     * @param y the Y component.
     * @return this vector.
     */
    public Vector3f setY(float y) {
        components[1] = y;
        return this;
    }

    /**
     * Set the Z component.
     *
     * @param z the Z component.
     * @return this vector.
     */
    public Vector3f setZ(float z) {
        components[2] = z;
        return this;
    }

    /**
     * Add the values to this vector.
     *
     * @param x x axis value.
     * @param y y axis value.
     * @param z z axis value.
     * @return this vector.
     */
    public Vector3f addLocal(float x, float y, float z) {
        add(floatVector(), fillOperation(x, y, z), components);
        return this;
    }

    /**
     * Add the vector to this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector3f addLocal(Vector3f vector) {
        return add(vector, this);
    }

    /**
     * Adds the vector from this vector and store the result to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    public Vector3f add(Vector3f vector, Vector3f result) {
        add(floatVector(), vector.floatVector(), result.components);
        return result;
    }

    /**
     * Adds the vector from this vector.
     *
     * @param vector the vector.
     * @return float vector (128bit)
     */
    public FloatVector add(Vector3f vector) {
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
     * @return this vector.
     */
    public Vector3f subtractLocal(float x, float y, float z) {
        subtract(floatVector(), fillOperation(x, y, z), components);
        return this;
    }

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector3f subtractLocal(Vector3f vector) {
        return subtract(vector, this);
    }

    /**
     * Subtract the vector from this vector and store the result to the result vector.
     *
     * @param vector the vector.
     * @param result the result.
     * @return the result vector.
     */
    public Vector3f subtract(Vector3f vector, Vector3f result) {
        subtract(floatVector(), vector.floatVector(), result.components);
        return result;
    }

    /**
     * Subtract the vector from this vector.
     *
     * @param vector the vector.
     * @return float vector (128bit)
     */
    public FloatVector subtract(Vector3f vector) {
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
    public Vector3f multLocal(float scalar) {
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
     * @return this vector.
     */
    public Vector3f multLocal(float x, float y, float z) {
        mult(floatVector(), fillOperation(x, y, z), components);
        return this;
    }

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector3f multLocal(Vector3f vector) {
        return mult(vector, this);
    }

    /**
     * Multiply this vector by the vector and store result to result vector.
     *
     * @param vector the vector.
     * @param result the result vector
     * @return result vector.
     */
    public Vector3f mult(Vector3f vector, Vector3f result) {
        mult(floatVector(), vector.floatVector(), result.components);
        return this;
    }

    /**
     * Multiply this vector by the vector.
     *
     * @param vector the vector.
     * @return float vector (128bit)
     */
    public FloatVector mult(Vector3f vector) {
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
    public Vector3f divideLocal(float scalar) {
        floatVector().div(scalar).intoArray(components, 0);
        return this;
    }

    /**
     * Divide this vector by the components.
     *
     * @param x the divider x.
     * @param y the divider y.
     * @param z the divider z.
     * @return this vector.
     */
    public Vector3f divideLocal(float x, float y, float z) {
        divide(floatVector(), fillOperation(x, y, z), components);
        return this;
    }

    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return this vector.
     */
    public Vector3f divideLocal(Vector3f vector) {
        return divide(vector, this);
    }

    /**
     * Divide this vector by the vector and store result to result vector.
     *
     * @param vector the divider vector.
     * @param result result vector
     * @return result vector.
     */
    public Vector3f divide(Vector3f vector, Vector3f result) {
        divide(floatVector(), vector.floatVector(), result.components);
        return this;
    }

    /**
     * Divide this vector by the vector.
     *
     * @param vector the divider vector.
     * @return float vector (128bit).
     */
    public FloatVector divide(Vector3f vector) {
        return floatVector().div(vector.floatVector());
    }

    private static void divide(FloatVector v1, FloatVector v2, float[] components) {
        v1.div(v2).intoArray(components, 0);
    }

    /**
     * Calculate a cross vector between this vector and the coordinates and store the result to this vector.
     *
     * @param x the other x.
     * @param y the other y.
     * @param z the other z.
     * @return this vector.
     */
    public Vector3f crossLocal(float x, float y, float z) {
        cross(floatVector(), fillOperation(x, y, z), components);
        return this;
    }

    /**
     * Calculate a cross vector between this vector and the vector.
     *
     * @param vector the vector.
     * @return this vector.
     */
    public Vector3f crossLocal(Vector3f vector) {
        return cross(vector, this);
    }

    /**
     * Calculate a cross vector between this vector and the coordinates and store the result to result vector.
     *
     * @param vector the vector.
     * @param result the result vector
     * @return result changed vector.
     */
    public Vector3f cross(Vector3f vector, Vector3f result) {
        cross(floatVector(), vector.floatVector(), result.components);
        return result;
    }

    /**
     * Calculate a cross vector between this vector and the vector.
     *
     * @param vector the vector
     * @return float vector (128bit)
     */
    public FloatVector cross(Vector3f vector) {
        return FloatVectorMath.cross128(floatVector(), vector.floatVector());
    }

    private static void cross(FloatVector v1, FloatVector v2, float[] components) {
        FloatVectorMath.cross128(v1, v2).intoArray(components, 0);
    }

    /**
     * Calculate perpendicular vector of this and store to this vector.
     *
     * @return this vector.
     */
    public Vector3f perpendicularLocal() {
        return perpendicular(this);
    }

    /**
     * Calculate perpendicular vector of this and store to result vector.
     *
     * @param result the result vector.
     * @return result vector.
     */
    public Vector3f perpendicular(Vector3f result) {
        return Math.abs(getX()) > Math.abs(getZ()) ?
            result.set(-getY(), getX(), 0) :
            result.set(0, -getZ(), getY());
    }

    /**
     * Calculate dot to the vector.
     *
     * @param vector the vector.
     * @return the dot product.
     */
    public float dot(Vector3f vector) {
        return FloatVectorMath.dot128(floatVector(), vector.floatVector());
    }

    /**
     * Calculate distance to the vector.
     *
     * @param vector the vector.
     * @return the distance.
     */
    public float distance(Vector3f vector) {
        return ExtMath.sqrt(distanceSquared(vector));
    }

    /**
     * Calculate squared distance to the coordinates.
     *
     * @param x the target x.
     * @param y the target y.
     * @param z the target z.
     * @return the squared distance.
     */
    public float distanceSquared(float x, float y, float z) {
        return distanceSquared(floatVector(), fillOperation(x, y, z), operation);
    }

    /**
     * Calculate squared distance to the vector.
     *
     * @param vector the vector.
     * @return the squared distance.
     */
    public float distanceSquared(Vector3f vector) {
        return distanceSquared(floatVector(), vector.floatVector(), operation);
    }

    private static float distanceSquared(FloatVector v1, FloatVector v2, float[] components) {
        var delta = v1.sub(v2);
        delta.mul(delta)
            .intoArray(components, 0);

        return components[0] + components[1] + components[2];
    }

    /**
     * Invert this vector to get a negative vector.
     *
     * @return this vector.
     */
    public Vector3f negateLocal() {
        return negate(this);
    }

    /**
     * Invert this vector and store to result vector.
     *
     * @param result result vector
     * @return result changed vector.
     */
    public Vector3f negate(Vector3f result) {
        floatVector().neg()
            .intoArray(result.components, 0);

        return result;
    }

    /**
     * Normalize this vector.
     *
     * @return this vector.
     */
    public Vector3f normalizeLocal() {
        return normalize(this);
    }

    /**
     * Normalize this vector and save result to result vector.
     *
     * @param result the result vector
     * @return result changed vector
     */
    public Vector3f normalize(Vector3f result) {
        float length = sqrLength();

        if (length != 1F && length != 0F) {
            length = 1.0F / ExtMath.sqrt(length);
            result.set(getX() * length, getY() * length, getZ() * length);
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
            components[2] * components[2];
    }

    /**
     * Move this vector to a new point by specified direction.
     *
     * @param direction move direction.
     * @param distance  move distance.
     * @return this vector.
     */
    public Vector3f moveToDirection(Vector3f direction, float distance) {
        direction.floatVector()
            .fma(fillOperation(distance, distance, distance), floatVector())
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
    public Vector3f moveToPoint(Vector3f destination, float distance) {
        var direction = destination.floatVector()
            .sub(floatVector());

        direction.mul(direction)
            .intoArray(operation, 0);

        float length = (float) Math.sqrt(operation[0] + operation[1] + operation[2]); // XXX double?
        if (length <= distance || length < ExtMath.EPSILON) {
            set(destination);
            return this;
        }

        direction.div(length) // normalize vector
            .fma(fillOperation(length, length, length), floatVector()) // Vn * Dn + Pn
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
    public Vector3f lerp(Vector3f min, Vector3f max, float t) {
        t = ExtMath.clamp(t);

        var minLines = min.floatVector();
        var delta = max.floatVector()
            .sub(minLines);

        minLines.fma(fillOperation(t, t, t), delta)
            .intoArray(components, 0);

        return this;
    }

    /**
     * Return angle in radians between this vector and other vector.
     *
     * @param other other vector
     * @return angle in radians
     */
    public float angle(Vector3f other) {
        float length = ExtMath.sqrt(sqrLength() * other.sqrLength());
        if (length < ExtMath.EPSILON) {
            return 0;
        }

        return ExtMath.acos(ExtMath.clamp(dot(other) / length, -1f, 1f));
    }

    /**
     * Rotate this vector by quaternion and store result of rotation to this vector.
     *
     * @param quaternion quaternion
     * @return this
     */
    public Vector3f rotateLocal(Quaternion quaternion) {
        return rotate(quaternion, this);
    }

    /**
     * Rotate this vector by quaternion and store result of rotation to result vector.
     *
     * @param quaternion quaternion
     * @param result result vector
     * @return result vector
     */
    public Vector3f rotate(Quaternion quaternion, Vector3f result) {
        // x = (1 - (ry * dy + rz * dz)) * x + (rx * dy - rw * dz) * y + (rx * dz + rw * dy) * z
        // y = (rx * dy + rw * dz) * x + (1 - (rx * dx + rz * dz)) * y + (ry * dz - rw * dx) * z
        // z = (rx * dz - rw * dy) * x + (ry * dz + rw * dx) * y + (1 - (rx * dx + ry * dy)) * z

        var d = quaternion.floatVector()
            .mul(2.0f)
            .toArray();

        var tmp1 = fillOperation(quaternion.getY(), quaternion.getX(), quaternion.getX())
            .mul(rearrange128(d, shuffle128(1, 1, 2, 3)))
            .add(fillOperation(quaternion.getZ(), quaternion.getW(), quaternion.getW())
                .mul(rearrange128(d, shuffle128(2, 2, 1, 3)))
                .lanewise(VectorOperators.NEG, mask128(false, false, true, false))
            ).lanewise(VectorOperators.NEG, mask128(true, false, false, false)) // inverse subtract operation (1 - a) to (-a + 1)
            .add(UNIT_X.floatVector())
            .mul(getX());

        var tmp2 = fillOperation(quaternion.getX(), quaternion.getX(), quaternion.getY())
            .mul(rearrange128(d, shuffle128(1, 0, 2, 3)))
            .add(fillOperation(quaternion.getW(), quaternion.getZ(), quaternion.getW())
                .mul(rearrange128(d, shuffle128(2, 2, 0, 3)))
                .lanewise(VectorOperators.NEG, mask128(true, false, false, false))
            ).lanewise(VectorOperators.NEG, mask128(false, true, false, false)) // inverse subtract operation (1 - a) to (-a + 1)
            .add(UNIT_Y.floatVector())
            .mul(getY());

        var tmp3 = fillOperation(quaternion.getX(), quaternion.getY(), quaternion.getX())
            .mul(rearrange128(d, shuffle128(2, 2, 0, 3)))
            .add(fillOperation(quaternion.getW(), quaternion.getW(), quaternion.getY())
                .mul(rearrange128(d, shuffle128(1, 0, 1, 3)))
                .lanewise(VectorOperators.NEG, mask128(false, true, false, false))
            ).lanewise(VectorOperators.NEG, mask128(false, false, true, false)) // inverse subtract operation (1 - a) to (-a + 1)
            .add(UNIT_Z.floatVector())
            .mul(getZ());

        tmp1.add(tmp2).add(tmp3)
            .intoArray(result.components, 0);

        return result;
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + Float.floatToIntBits(components[0]);
        result = prime * result + Float.floatToIntBits(components[1]);
        result = prime * result + Float.floatToIntBits(components[2]);
        return result;
    }

    @Override
    public Vector3f clone() {
        try {
            return (Vector3f) super.clone();
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

        var other = (Vector3f) obj;
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
    public boolean equals(Vector3f vector, float epsilon) {
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
        return "Vector3f(x: " + getX() + ", y: " + getY() + ", z: " + getZ() + ')';
    }

    private FloatVector fillOperation(float x, float y, float z) {
        operation[0] = x;
        operation[1] = y;
        operation[2] = z;
        return FloatVector.fromArray(SPECIES, operation, 0);
    }

}
package ru.finex.core.math;

import jdk.incubator.vector.VectorOperators;
import ru.finex.core.math.vector.Vector3f;
import ru.finex.core.math.vector.Vector4f;

import static ru.finex.core.math.FloatVectorMath.cross128;
import static ru.finex.core.math.FloatVectorMath.mask128;
import static ru.finex.core.math.FloatVectorMath.normalize128;
import static ru.finex.core.math.FloatVectorMath.rearrange128;
import static ru.finex.core.math.FloatVectorMath.shuffle128;

/**
 * @author m0nster.mind
 */
public final class Quaternion extends Vector4f {

    public static final Quaternion UNIT_W = new Quaternion(0, 0, 0, 1);

    public Quaternion() {
        super();
    }

    public Quaternion(float x, float y, float z, float w) {
        super(x, y, z, w);
    }

    public Quaternion(float[] components) {
        super(components);
    }

    public Quaternion(Vector4f another) {
        super(another);
    }

    public Quaternion(float eulerX, float eulerY, float eulerZ) {
        setAngles(eulerX, eulerY, eulerZ);
    }

    /**
     * Quaternion without rotation ({@link #UNIT_W UNIT_W}).
     * @return read-only quaternion
     */
    public static Quaternion identity() {
        return UNIT_W;
    }

    /**
     * Set euler angles to this quaternion.
     *
     * @param x roll
     * @param y pitch
     * @param z yaw
     * @return this quaternion
     */
    @SuppressWarnings("checkstyle:ParameterAssignment")
    public Quaternion setAngles(float x, float y, float z) {
        x /= 2.0f; // phi
        y /= 2.0f; // the
        z /= 2.0f; // psi

        float[] cosines = fillOperation(x, y, z, 0)
            .lanewise(VectorOperators.COS)
            .toArray(); // FIXME m0nster.mind: array allocation

        float[] sinuses = fillOperation(x, y, z, 0)
            .lanewise(VectorOperators.SIN)
            .toArray(); // FIXME m0nster.mind: array allocation

        // x = cos(the) * cos(psi) * sin(phi) - sin(the) * sin(psi) * cos(phi);
        // y = cos(phi) * cos(psi) * sin(the) + sin(phi) * sin(psi) * cos(the);
        // z = cos(phi) * cos(the) * sin(psi) - sin(phi) * sin(the) * cos(psi);
        // w = cos(phi) * cos(the) * cos(psi) + sin(phi) * sin(the) * sin(psi);

        var tmp = rearrange128(cosines, shuffle128(1, 0, 0, 0))
            .mul(rearrange128(cosines, shuffle128(2, 2, 1, 1)))
            .mul(rearrange128(sinuses, shuffle128(0, 1, 2, 3))
                .withLane(3, cosines[2])
            );

        var tmp2 = rearrange128(sinuses, shuffle128(1, 0, 0, 0))
            .mul(rearrange128(sinuses, shuffle128(2, 2, 1, 1)))
            .mul(rearrange128(cosines, shuffle128(0, 1, 2, 3))
                .withLane(3, sinuses[2]))
            .lanewise(VectorOperators.NEG, mask128(true, false, true, false));

        tmp.add(tmp2).intoArray(components, 0);

        float length = length();
        if (length < ExtMath.EPSILON) {
            set(UNIT_W);
        } else {
            divideLocal(length); // normalize
        }

        return this;
    }

    /**
     * Combine rotations of this quaternion and other quaternion, store result rotation to this quaternion.
     * Its alias of {@link Vector4f#crossLocal(Vector4f) cross product}.
     *
     * @param quaternion other quaternion.
     * @return this
     */
    public Quaternion combineLocal(Quaternion quaternion) {
        return combine(quaternion, this);
    }

    /**
     * Combine rotation of this quaternion and other quaternion, store result rotation to the result.
     *
     * @param quaternion other quaternion
     * @param result the result
     * @return result
     */
    public Quaternion combine(Quaternion quaternion, Quaternion result) {
        cross(quaternion, result);
        return result;
    }

    /**
     * Rotate this quaternion to specified forward direction with Y-axis upward.
     *
     * @param direction forward direction
     * @return this
     */
    public Quaternion lookAtLocal(Vector3f direction) {
        return lookAt(direction, Vector3f.UNIT_Y, this);
    }

    /**
     * Rotate this quaternion to specified forward direction and specified upward direction.
     *
     * @param direction forward direction
     * @param upAxis upward direction
     * @return this
     */
    public Quaternion lookAtLocal(Vector3f direction, Vector3f upAxis) {
        return lookAt(direction, upAxis, this);
    }

    /**
     * Rotate this quaternion to specified forward direction with Y-axis upward, and store
     *  rotation result to result quaternion.
     *
     * @param direction forward direction
     * @param result result quaternion
     * @return result quaternion
     */
    public Quaternion lookAt(Vector3f direction, Quaternion result) {
        return lookAt(direction, Vector3f.UNIT_Y, result);
    }

    /**
     * Rotate this quaternion to specified forward direction and specified upward direction, and store
     *  rotation result to result quaternion.
     *
     * @param direction forward direction
     * @param upAxis upward direction
     * @param result result quaternion
     * @return result quaternion
     */
    public Quaternion lookAt(Vector3f direction, Vector3f upAxis, Quaternion result) {
        var axis = normalize128(upAxis.floatVector());
        return lookAt(direction.getComponents(), cross128(direction.floatVector(), axis).toArray(), axis.toArray(), result);
    }

    /**
     * Rotate this quaternion to direction from position to target and specified upward direction, and store
     *  rotation result to result quaternion.
     *
     * @param position position
     * @param transform target
     * @param upAxis upward direction
     * @param result result quaternion
     * @return result quaternion
     */
    public Quaternion lookAt(Vector3f position, Vector3f transform, Vector3f upAxis, Quaternion result) {
        return lookAt(position.getComponents(), transform.getComponents(), normalize128(upAxis.floatVector()).toArray(), result);
    }

    /**
     * Rotate this quaternion to direction from position to target and specified upward direction, and store
     *  rotation result to result quaternion.
     *
     * @param m0 position
     * @param m1 target position
     * @param m2 upward direction
     * @param result result quaternion
     * @return result
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    private static Quaternion lookAt(float[] m0, float[] m1, float[] m2, Quaternion result) {
        float trace = m0[0] + m1[1] + m2[2];
        if (trace > 0f) {
            float s = 0.5f / ExtMath.sqrt(trace + 1f);
            result.components[0] = (m2[1] - m1[2]) * s;
            result.components[1] = (m0[2] - m2[0]) * s;
            result.components[2] = (m1[0] - m0[1]) * s;
            result.components[3] = 0.25f / s;
        } else if (m0[0] > m1[1] && m0[0] > m2[2]) {
            float s = ExtMath.sqrt(1f + m0[0] - m1[1] - m2[2]) * 2f;
            result.components[0] = s * 0.25f;
            result.components[1] = (m0[1] + m1[0]) / s;
            result.components[2] = (m0[2] + m2[0]) / s;
            result.components[3] = (m2[1] - m1[2]) / s;
        } else if (m1[1] > m2[2]) {
            float s = ExtMath.sqrt(1f + m1[1] - m0[0] - m2[2]) * 2f;
            result.components[0] = (m0[1] + m1[0]) / s;
            result.components[1] = s * 0.25f;
            result.components[2] = (m1[2] + m2[1]) / s;
            result.components[3] = (m0[2] - m2[0]) / s;
        } else {
            float s = ExtMath.sqrt(1f + m2[2] - m0[0] - m1[1]) * 2f;
            result.components[0] = (m0[2] + m2[0]) / s;
            result.components[1] = (m1[2] + m2[1]) / s;
            result.components[2] = s * 0.25f;
            result.components[3] = (m1[0] - m0[1]) / s;
        }

        result.normalizeLocal();
        return result;
    }

    /**
     * Spherical linear-based interpolation (shortest path) between this quaternion and to quaternion,
     *  stored to this quaternion.
     * Requires normalize this quaternion!
     *
     * @param to from quaternion (normalized)
     * @param t time, between 0 and 1
     * @return this
     */
    public Quaternion slerp(Quaternion to, float t) {
        return slerp(this, to, t);
    }

    /**
     * Spherical linear-based interpolation (shortest path) between from quaternion and to quaternion,
     *  stored to this quaternion.
     *
     * @param from from quaternion (normalized)
     * @param to to quaternion (normalized)
     * @param t time, between 0 and 1
     * @return this
     */
    @SuppressWarnings("checkstyle:MagicNumber")
    public Quaternion slerp(Quaternion from, Quaternion to, float t) {
        if (t >= 1f) {
            set(to);
            return this;
        }

        // d = q1 * q2
        // qa = d >= 0 ? q1 : -q1
        // the = arccos(|d|)
        // slerp = (sin((1 - t) * the) * qa + sin(t*the) * q2) / sin(the)

        float d = from.distanceSquared(to);
        float the = ExtMath.acos(d);
        float invSinThe = 1f / ExtMath.sin(the);
        float tmp1 = ExtMath.sin((1f - t) * the) * invSinThe;
        float tmp2 = ExtMath.sin(t * the) * invSinThe;

        var toLanes = to.floatVector();
        if (d < 0f) {
            toLanes = toLanes.neg();
        }

        from.floatVector()
            .mul(tmp1)
            .add(toLanes.mul(tmp2))
            .intoArray(components, 0);

        return this;
    }

    /**
     * Store quaternion direction to result vector.
     *
     * @param result result vector
     * @return result
     */
    public Vector3f getDirection(Vector3f result) {
        return result.set(Vector3f.UNIT_X)
            .rotateLocal(this);
    }

    // TODO m0nster.mind: impl slerp with lerp fallback in small diff between both quaternions or inverted target quaternion:
    //  d = q1 * q2
    //  qa = d >= 0 ? q1 : -q1
    //  if (|d| >= 1 - (e / 2)) => (1 - t)*qa + t*q2
    //  else => the = arccos(|d|), (sin((1 - t) * the) * qa + sin(t*the) * q2) / sin(the)

    @Override
    public String toString() {
        return "Quaternion(x: " + getX() + ", y: " + getY() + ", z: " + getZ() + ", w: " + getW() + ")";
    }

}

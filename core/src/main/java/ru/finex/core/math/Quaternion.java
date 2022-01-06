package ru.finex.core.math;

import jdk.incubator.vector.VectorOperators;
import ru.finex.core.math.vector.Vector4f;

import static ru.finex.core.math.FloatVectorMath.mask128;
import static ru.finex.core.math.FloatVectorMath.rearrange128;
import static ru.finex.core.math.FloatVectorMath.shuffle128;

/**
 * @author m0nster.mind
 */
public class Quaternion extends Vector4f {

    public static final Quaternion UNIT_W = new Quaternion(0, 0, 0, 1);

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
     * @param quaternion other quaternion.
     * @return this
     */
    public Quaternion combineLocal(Quaternion quaternion) {
        return combine(quaternion, this);
    }

    /**
     * Combine rotation of this quaternion and other quaternion, store result rotation to the result.
     * @param quaternion other quaternion
     * @param result the result
     * @return result
     */
    public Quaternion combine(Quaternion quaternion, Quaternion result) {
        cross(quaternion, result);
        return result;
    }

}

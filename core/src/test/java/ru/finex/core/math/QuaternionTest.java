package ru.finex.core.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.finex.core.math.vector.Vector3f;

import static ru.finex.core.math.vector.VectorTestUtils.assertEquals;

/**
 * @author m0nster.mind
 */
public class QuaternionTest {

    private static final Vector3f[] LOOK_DIRECTIONS = {
        Vector3f.UNIT_X,
        Vector3f.UNIT_Z,
        Vector3f.UNIT_X_NEGATIVE,
        Vector3f.UNIT_Z_NEGATIVE
    };

    private static final Arguments[] SLERP_ARGUMENTS = {
        Arguments.of(0.0f, new Quaternion(0f, 0.7071067f, 0f, 0.7071068f)),
        Arguments.of(0.1f, new Quaternion(0f, 0.77214915f, 0f, 0.65572965f)),
        Arguments.of(0.2f, new Quaternion(0f, 0.830302f, 0f, 0.59850174f)),
        Arguments.of(0.3f, new Quaternion(0f, 0.8810464f, 0f, 0.5359335f)),
        Arguments.of(0.4f, new Quaternion(0f, 0.92392963f, 0f, 0.46858346f)),
        Arguments.of(0.5f, new Quaternion(0f, 0.95856893f, 0f, 0.3970524f)),
        Arguments.of(0.6f, new Quaternion(0f, 0.9846554f, 0f, 0.3219786f)),
        Arguments.of(0.7f, new Quaternion(0f, 1.0019561f, 0f, 0.24403194f)),
        Arguments.of(0.8f, new Quaternion(0f, 1.0103168f, 0f, 0.16390784f)),
        Arguments.of(0.9f, new Quaternion(0f, 1.0096629f, 0f, 0.0823213f)),
        Arguments.of(1.0f, new Quaternion(0f, 1f, 0f, 0f))
    };

    public static Vector3f[] getLookDirections() {
        return LOOK_DIRECTIONS;
    }

    public static Arguments[] getSlerpArguments() {
        return SLERP_ARGUMENTS;
    }

    @MethodSource("getLookDirections")
    @ParameterizedTest
    public void lookAtTest(Vector3f forward) {
        Quaternion quaternion = new Quaternion();
        quaternion.lookAtLocal(forward);

        Vector3f vector = new Vector3f(Vector3f.UNIT_X);
        vector *= quaternion;

        System.out.println(quaternion);
        System.out.println(forward);
        System.out.println(vector);
        assertEquals(forward, vector, 0.001f);
    }

    @Test
    public void eulerAnglesAndRotateVector() {
        Quaternion quaternion = new Quaternion(0, 0, ExtMath.toRad(180f));
        Vector3f forward = new Vector3f(Vector3f.UNIT_X);
        forward *= quaternion;
        forward.normalizeLocal();

        Vector3f expected = Vector3f.UNIT_X_NEGATIVE;
        assertEquals(expected, forward, 0.001f);
    }

    @ParameterizedTest
    @MethodSource("getSlerpArguments")
    public void slerpTest(float t, Quaternion expected) {
        Quaternion quaternion = new Quaternion();

        Quaternion from = new Quaternion(0, ExtMath.toRad(90f), 0);
        Quaternion to = new Quaternion(0, ExtMath.toRad(180f), 0);

        assertEquals(expected, quaternion.slerp(from, to, t), 0.001f);
    }

}

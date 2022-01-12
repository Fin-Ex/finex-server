package ru.finex.core.math;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.finex.core.math.vector.Vector3f;

import static ru.finex.core.math.vector.VectorUtils.assertEquals;

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

    public static Vector3f[] getLookDirections() {
        return LOOK_DIRECTIONS;
    }

    @MethodSource("getLookDirections")
    @ParameterizedTest
    public void lookAtTest(Vector3f forward) {
        Quaternion quaternion = new Quaternion();
        quaternion.lookAtLocal(forward);

        Vector3f vector = new Vector3f(Vector3f.UNIT_X);
        vector.rotateLocal(quaternion);

        System.out.println(quaternion);
        System.out.println(forward);
        System.out.println(vector);
        assertEquals(forward, vector, 0.001f);
    }

    @Test
    public void eulerAnglesAndRotateVector() {
        Quaternion quaternion = new Quaternion(0, 0, ExtMath.toRad(180f));
        Vector3f forward = new Vector3f(Vector3f.UNIT_X);
        forward.rotateLocal(quaternion).normalizeLocal();

        Vector3f expected = Vector3f.UNIT_X_NEGATIVE;
        assertEquals(expected, forward, 0.001f);
    }

}

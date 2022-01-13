package ru.finex.core.math.vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.finex.core.math.Quaternion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.finex.core.math.vector.VectorUtils.assertEquals;

/**
 * @author m0nster.mind
 */
public class Vector3fTest {

    private static final Arguments[] ROT_DIRECTIONS = {
        Arguments.of(new Quaternion(0, 0, 0, 1f), Vector3f.UNIT_X),
        Arguments.of(new Quaternion(0, 0.7071f, 0, 0.7071f), Vector3f.UNIT_Z_NEGATIVE),
        Arguments.of(new Quaternion(0, 1f, 0, 0), Vector3f.UNIT_X_NEGATIVE),
        Arguments.of(new Quaternion(0, -0.7071f, 0, 0.7071f), Vector3f.UNIT_Z)
    };

    private static final Arguments[] MTP_TARGETS = {
        Arguments.of(new Vector3f(20f, 20f, 0), 10f, new Vector3f(7.071f, 7.071f, 0)),
        Arguments.of(new Vector3f(5f, 5f, 0), 20f, new Vector3f(5f, 5f, 0)),
    };

    public static Arguments[] getRotateDirections() {
        return ROT_DIRECTIONS;
    }

    public static Arguments[] getMtpTargets() {
        return MTP_TARGETS;
    }

    @Test
    public void dotTest() {
        Vector3f first = new Vector3f(10f, 5f, 0f);
        Vector3f second = new Vector3f(2f, 5f, 1f);

        assertEquals(45f, first.dot(second), 0.001f);
    }

    @Test
    public void crossTest() {
        Vector3f x = new Vector3f(Vector3f.UNIT_X);
        Vector3f y = new Vector3f(Vector3f.UNIT_Y);

        Vector3f xy = new Vector3f();
        x.cross(y, xy);

        Vector3f z = Vector3f.UNIT_Z;
        System.out.println(z);
        System.out.println(xy);
        assertEquals(z, xy, 0.001f);
    }

    @MethodSource("getRotateDirections")
    @ParameterizedTest
    public void rotateTest(Quaternion quaternion, Vector3f forward) {
        Vector3f direction = new Vector3f(Vector3f.UNIT_X);
        direction.rotateLocal(quaternion);

        System.out.println(quaternion);
        System.out.println(forward);
        System.out.println(direction);
        assertEquals(forward, direction, 0.001f);
    }

    @Test
    public void normalizeTest() {
        Vector3f vector = new Vector3f(10f, 5f, 0);
        vector.normalizeLocal();

        assertEquals(new Vector3f(0.894427f, 0.447214f, 0f), vector, 0.001f);
    }

    @Test
    public void moveToDirectionTest() {
        Vector3f vector = new Vector3f();
        Vector3f direction = new Vector3f(0.5f, 0.5f, 0);

        vector.moveToDirection(direction, 10f);
        assertEquals(new Vector3f(5f, 5f, 0), vector, 0.001f);
    }

    @MethodSource("getMtpTargets")
    @ParameterizedTest
    public void moveToPointTest(Vector3f target, float distance, Vector3f result) {
        Vector3f vector = new Vector3f();
        vector.moveToPoint(target, distance);

        assertEquals(result, vector, 0.001f);
    }

}

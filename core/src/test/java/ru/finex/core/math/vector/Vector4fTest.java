package ru.finex.core.math.vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.finex.core.math.ExtMath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.finex.core.math.vector.VectorUtils.assertEquals;

/**
 * @author oracle
 */
public class Vector4fTest {

    private static final Arguments[] MTP_VARIATIONS = {
        Arguments.of(new Vector4f(10f, 10f, 0f, 0f), 5f, new Vector4f(3.535534f, 3.535534f, 0f, 0f)),
        Arguments.of(new Vector4f(10f, 10f, 0f, 0f), 10f, new Vector4f(7.071068f, 7.071068f, 0f, 0f)),
        Arguments.of(new Vector4f(10f, 10f, 0f, 0f), 20f, new Vector4f(10f, 10f, 0f, 0f))
    };

    private static final Arguments[] ANGLE_RELATIONS = {
        Arguments.of(Vector4f.UNIT_X, Vector4f.UNIT_Y, ExtMath.HALF_PI),
        Arguments.of(Vector4f.UNIT_X, Vector4f.UNIT_X_NEGATIVE, ExtMath.PI),
        Arguments.of(Vector4f.UNIT_XYZ, Vector4f.UNIT_XYZ, ExtMath.ZERO_FLOAT),
        Arguments.of(Vector4f.UNIT_XYZW, Vector4f.UNIT_XYZW_NEGATIVE, ExtMath.PI)
    };

    private static final Arguments[] LERP_ARGS = {
        Arguments.of(0.0f, new Vector4f(3f, 3f, 3f, 3f)),
        Arguments.of(0.1f, new Vector4f(3.9f, 3.9f, 3.9f, 3.9f)),
        Arguments.of(0.2f, new Vector4f(4.8f, 4.8f, 4.8f, 4.8f)),
        Arguments.of(0.3f, new Vector4f(5.7f, 5.7f, 5.7f, 5.7f)),
        Arguments.of(0.4f, new Vector4f(6.6f, 6.6f, 6.6f, 6.6f)),
        Arguments.of(0.5f, new Vector4f(7.5f, 7.5f, 7.5f, 7.5f)),
        Arguments.of(0.6f, new Vector4f(8.4f, 8.4f, 8.4f, 8.4f)),
        Arguments.of(0.7f, new Vector4f(9.3f, 9.3f, 9.3f, 9.3f)),
        Arguments.of(0.8f, new Vector4f(10.2f, 10.2f, 10.2f, 10.2f)),
        Arguments.of(0.9f, new Vector4f(11.1f, 11.1f, 11.1f, 11.1f)),
        Arguments.of(1.0f, new Vector4f(12f, 12f, 12f, 12f))
    };

    protected static Arguments[] getMtpVariations() {
        return MTP_VARIATIONS;
    }

    protected static Arguments[] getAngleRelations() {
        return ANGLE_RELATIONS;
    }

    protected static Arguments[] getLerpArgs() {
        return LERP_ARGS;
    }

    @Test
    public void dotTest() {
        Vector4f first = new Vector4f(5f, 3f, 2f, 6f);
        Vector4f second = new Vector4f(2f, 4f, 7f, 3f);

        assertEquals(54f, first.dot(second), 0.001f);
    }

    @Test
    public void crossTest() {
        Vector4f x = new Vector4f(2f, 3f, 1f, 5f);
        Vector4f y = new Vector4f(1f, 4f, 2f, 6f);

        assertEquals(new Vector4f(19f, 35f, 21f, 14f), x % y, 0.001f);
    }

    @Test
    public void normalizeTest() {
        Vector4f vector4f = new Vector4f(5f, 3f, 2f, 6f);
        vector4f.normalizeLocal();

        assertEquals(new Vector4f(0.581238f, 0.348743f, 0.232495f, 0.697486f), vector4f, 0.001f);
    }

    @Test
    public void moveToDirectionTest() {
        Vector4f vector4f = new Vector4f();
        Vector4f direction = new Vector4f(1f, -1f, 1f, -1f);

        vector4f.moveToDirection(direction, 10f);

        assertEquals(new Vector4f(10f, -10f, 10f, -10f), vector4f, 0.001f);
    }

    @ParameterizedTest
    @MethodSource("getMtpVariations")
    public void moveToPointTest(Vector4f destination, float distance, Vector4f expected) {
        Vector4f vector4f = new Vector4f();
        vector4f.moveToPoint(destination, distance);

        assertEquals(expected, vector4f, 0.001f);
    }

    @ParameterizedTest
    @MethodSource("getAngleRelations")
    public void angleTest(Vector4f first, Vector4f second, float expectedAngleRad) {
        assertEquals(expectedAngleRad, first.angle(second), 0.001f);
    }

    @ParameterizedTest
    @MethodSource("getLerpArgs")
    public void lerpTest(float t, Vector4f expected) {
        Vector4f vector4f = new Vector4f();

        Vector4f min = new Vector4f(3f, 3f, 3f, 3f);
        Vector4f max = new Vector4f(12f, 12f, 12f, 12f);

        assertEquals(expected, vector4f.lerp(min, max, t), 0.001f);
    }

}

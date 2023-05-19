package ru.finex.core.math.vector;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.finex.core.math.ExtMath;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.finex.core.math.vector.VectorTestUtils.assertEquals;

/**
 * @author oracle
 */
public class Vector2fTest {

    private static final Arguments[] ANGLE_RELATIONS = {
        Arguments.of(new Vector2f(Vector2f.UNIT_X), new Vector2f(Vector2f.UNIT_X), ExtMath.ZERO_FLOAT),
        Arguments.of(new Vector2f(Vector2f.UNIT_X), new Vector2f(Vector2f.UNIT_XY), ExtMath.QUARTER_PI),
        Arguments.of(new Vector2f(Vector2f.UNIT_X), new Vector2f(Vector2f.UNIT_Y), ExtMath.HALF_PI),
        Arguments.of(new Vector2f(Vector2f.UNIT_X), new Vector2f(Vector2f.UNIT_XY_NEGATIVE), ExtMath.QUARTER_PI + ExtMath.HALF_PI),
        Arguments.of(new Vector2f(Vector2f.UNIT_X), new Vector2f(Vector2f.UNIT_X_NEGATIVE), ExtMath.PI),
        Arguments.of(new Vector2f(Vector2f.UNIT_X), new Vector2f(Vector2f.UNIT_Y_NEGATIVE), ExtMath.HALF_PI)
    };

    private static final Arguments[] LERP_VARIATIONS = {
        Arguments.of(0.0f, new Vector2f(2.0f, 2.0f)),
        Arguments.of(0.1f, new Vector2f(2.8f, 2.8f)),
        Arguments.of(0.2f, new Vector2f(3.6f, 3.6f)),
        Arguments.of(0.3f, new Vector2f(4.4f, 4.4f)),
        Arguments.of(0.4f, new Vector2f(5.2f, 5.2f)),
        Arguments.of(0.5f, new Vector2f(6.0f, 6.0f)),
        Arguments.of(0.6f, new Vector2f(6.8f, 6.8f)),
        Arguments.of(0.7f, new Vector2f(7.6f, 7.6f)),
        Arguments.of(0.8f, new Vector2f(8.4f, 8.4f)),
        Arguments.of(0.9f, new Vector2f(9.2f, 9.2f)),
        Arguments.of(1.0f, new Vector2f(10.0f, 10.0f))
    };

    private static final Arguments[] MTP_VARIATIONS = {
        Arguments.of(new Vector2f(10f, 10f), 5f, new Vector2f(3.535534f, 3.535534f)),
        Arguments.of(new Vector2f(10f, 10f), 10f, new Vector2f(7.071068f, 7.071068f)),
        Arguments.of(new Vector2f(10f, 10f), 20f, new Vector2f(10.0f, 10.0f)),
        Arguments.of(new Vector2f(10f, 10f), 40f, new Vector2f(10.0f, 10.0f)),
    };

    protected static Arguments[] getAngleRelations() {
        return ANGLE_RELATIONS;
    }

    protected static Arguments[] getLerpVariations() {
        return LERP_VARIATIONS;
    }

    protected static Arguments[] getMtpVariations() {
        return MTP_VARIATIONS;
    }

    @Test
    public void dotTest() {
        Vector2f first = new Vector2f(6f, 5f);
        Vector2f second = new Vector2f(4f, 10f);

        assertEquals(74f, first.dot(second), 0.001f);
    }

    @Test
    public void crossTest() {
        Vector2f x = new Vector2f(Vector2f.UNIT_X);
        Vector2f y = new Vector2f(Vector2f.UNIT_Y);

        assertEquals(1f, x % y, 0.001f);
    }

    @Test
    public void normalizeTest() {
        Vector2f vector2f = new Vector2f(6f, 5f);
        vector2f.normalizeLocal();

        assertEquals(new Vector2f(0.768221f, 0.640184f), vector2f, 0.001f);
    }

    @Test
    public void moveToDirectionTest() {
        Vector2f vector2f = new Vector2f();
        Vector2f direction = new Vector2f(1f, 1f);

        vector2f.moveToDirection(direction, 10f);

        assertEquals(new Vector2f(10f, 10f), vector2f, 0.001f);
    }

    @ParameterizedTest
    @MethodSource("getMtpVariations")
    public void moveToPointTest(Vector2f destination, float distance, Vector2f expectedVector) {
        Vector2f vector2f = new Vector2f();
        vector2f.moveToPoint(destination, distance);

        assertEquals(expectedVector, vector2f, 0.001f);
    }

    @ParameterizedTest
    @MethodSource("getAngleRelations")
    public void angleTest(Vector2f firstVector, Vector2f secondVector, float expectedAngle) {
        assertEquals(expectedAngle, firstVector.angle(secondVector), 0.001f);
    }

    @ParameterizedTest
    @MethodSource("getLerpVariations")
    public void lerpTest(float t, Vector2f expectedVector) {
        Vector2f vector2f = new Vector2f();

        Vector2f min = new Vector2f(2f, 2f);
        Vector2f max = new Vector2f(10f, 10f);

        assertEquals(expectedVector, vector2f.lerp(min, max, t), 0.001f);
    }

    @Test
    public void perpendicularTest() {
        Vector2f vector2f = new Vector2f(6f, 5f);
        vector2f.perpendicularLocal();

        assertEquals(new Vector2f(5f, -6f), vector2f, 0.001f);
    }

}

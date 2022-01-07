package ru.finex.core.math.vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.finex.core.math.Quaternion;

/**
 * @author m0nster.mind
 */
public class Vector3fTest {

    private static final Arguments[] DIRECTIONS = {
        Arguments.of(new Quaternion(0, 0, 0, 1f), Vector3f.UNIT_X),
        Arguments.of(new Quaternion(0, 0.7071f, 0, 0.7071f), Vector3f.UNIT_Z_NEGATIVE),
        Arguments.of(new Quaternion(0, 1f, 0, 0), Vector3f.UNIT_X_NEGATIVE),
        Arguments.of(new Quaternion(0, -0.7071f, 0, 0.7071f), Vector3f.UNIT_Z)
    };

    public static Arguments[] getDirections() {
        return DIRECTIONS;
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
        Assertions.assertEquals(z.getX(), xy.getX(), 0.001f);
        Assertions.assertEquals(z.getY(), xy.getY(), 0.001f);
        Assertions.assertEquals(z.getZ(), xy.getZ(), 0.001f);
    }

    @MethodSource("getDirections")
    @ParameterizedTest
    public void rotateTest(Quaternion quaternion, Vector3f forward) {
        Vector3f direction = new Vector3f(Vector3f.UNIT_X);
        direction.rotateLocal(quaternion);

        System.out.println(quaternion);
        System.out.println(forward);
        System.out.println(direction);
        Assertions.assertEquals(forward.getX(), direction.getX(), 0.001f);
        Assertions.assertEquals(forward.getY(), direction.getY(), 0.001f);
        Assertions.assertEquals(forward.getZ(), direction.getZ(), 0.001f);
    }

}

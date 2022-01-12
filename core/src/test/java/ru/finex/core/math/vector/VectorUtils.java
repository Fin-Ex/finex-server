package ru.finex.core.math.vector;

import lombok.experimental.UtilityClass;
import org.junit.jupiter.api.Assertions;

/**
 * @author m0nster.mind
 */
@UtilityClass
public class VectorUtils {

    public static void assertEquals(Vector3f expected, Vector3f actual, float epsilon) {
        Assertions.assertEquals(expected.getX(), actual.getX(), epsilon);
        Assertions.assertEquals(expected.getY(), actual.getY(), epsilon);
        Assertions.assertEquals(expected.getZ(), actual.getZ(), epsilon);
    }

    public static void assertEquals(Vector2f expected, Vector2f actual, float epsilon) {
        Assertions.assertEquals(expected.getX(), actual.getX(), epsilon);
        Assertions.assertEquals(expected.getY(), actual.getY(), epsilon);
    }

    public static void assertEquals(Vector4f expected, Vector4f actual, float epsilon) {
        Assertions.assertEquals(expected.getX(), actual.getX(), epsilon);
        Assertions.assertEquals(expected.getY(), actual.getY(), epsilon);
        Assertions.assertEquals(expected.getZ(), actual.getZ(), epsilon);
        Assertions.assertEquals(expected.getW(), actual.getW(), epsilon);
    }

}

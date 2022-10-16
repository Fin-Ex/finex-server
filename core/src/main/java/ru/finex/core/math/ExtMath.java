package ru.finex.core.math;

import lombok.experimental.UtilityClass;
import ru.finex.core.math.vector.Vector3f;

/**
 * Extended math class.
 *
 * @author JavaSaBr
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:returnCount")
@UtilityClass
public class ExtMath {

    public static final Float ZERO_FLOAT = 0F;
    public static final Double ZERO_DOUBLE = 0D;
    public static final Integer ZERO_INTEGER = 0;
    public static final Long ZERO_LONG = 0L;

    /**
     * The value PI as a float. (180 degrees).
     */
    public static final float PI = (float) Math.PI;

    /**
     * The value PI/2 as a float. (90 degrees)
     */
    public static final float HALF_PI = 0.5f * PI;

    /**
     * The value PI/4 as a float. (45 degrees)
     */
    public static final float QUARTER_PI = 0.25f * PI;

    public static final double EPSILON = 1.40129846432482E-45;

    /**
     * Constant by which to multiply an angular value in degrees to obtain an
     * angular value in radians.
     */
    public static final float DEG_TO_RAD = 0.01745329f;

    /**
     * Constant by which to multiply an angular value in radians to obtain an
     * angular value in degrees.
     */
    public static final float RAD_TO_DEG = 57.29577951f;

    /**
     * Return the arc cosine of a value.<br/> Special cases: <ul><li>If value is smaller than -1, then the result is PI.
     * <li>If the argument is greater than 1, then the result is 0.</ul>
     *
     * @param value The value to arc cosine.
     * @return The angle, in radians.
     * @see java.lang.Math#acos(double) java.lang.Math#acos(double)
     */
    public static float acos(float value) {
        if (-1.0F < value) {
            if (value < 1.0f) {
                return (float) Math.acos(value);
            }
            return 0.0F;
        }

        return PI;
    }

    /**
     * Return the arc sine of a value.<br/> Special cases: <ul> <li>If value is smaller than -1, then the result is
     * -HALF_PI. <li>If the argument is greater than 1, then the result is HALF_PI. </ul>
     *
     * @param value The value to arc sine.
     * @return the angle in radians.
     * @see java.lang.Math#asin(double) java.lang.Math#asin(double)
     */
    public static float asin(float value) {
        if (-1.0F < value) {
            if (value < 1.0F) {
                return (float) Math.asin(value);
            }

            return HALF_PI;
        }

        return -HALF_PI;
    }

    /**
     * A direct call to Math.atan2.
     *
     * @param y the y
     * @param x the x
     * @return Math.atan2(y, x) float
     * @see java.lang.Math#atan2(double, double) java.lang.Math#atan2(double, double)
     */
    public static float atan2(float y, float x) {
        return (float) Math.atan2(y, x);
    }

    /**
     * Return cosine of an angle. Direct call to java.lang.Math
     *
     * @param value The angle to cosine.
     * @return the cosine of the angle.
     * @see Math#cos(double) Math#cos(double)
     */
    public static float cos(float value) {
        return (float) Math.cos(value);
    }

    /**
     * Return 1/sqrt(value).
     *
     * @param value The value to process.
     * @return 1 /sqrt(value)
     * @see java.lang.Math#sqrt(double) java.lang.Math#sqrt(double)
     */
    public static float invSqrt(float value) {
        return (float) (1.0f / Math.sqrt(value));
    }

    /**
     * Return the sine of an angle. Direct call to java.lang.Math
     *
     * @param value The angle to sine.
     * @return the sine of the angle.
     * @see Math#sin(double) Math#sin(double)
     */
    public static float sin(float value) {
        return (float) Math.sin(value);
    }

    /**
     * Returns the square root of a given value.
     *
     * @param value The value to sqrt.
     * @return The square root of the given value.
     * @see java.lang.Math#sqrt(double) java.lang.Math#sqrt(double)
     */
    public static float sqrt(float value) {
        return (float) Math.sqrt(value);
    }

    /**
     * Return true of the value is zero.
     *
     * @param value the value.
     * @return true if the values are equals.
     */
    public static boolean isZero(float value) {
        return Float.compare(value, 0F) == 0;
    }

    /**
     * Return true of the value is zero.
     *
     * @param value the value.
     * @return true if the values are equals.
     */
    public static boolean isZero(double value) {
        return Double.compare(value, 0D) == 0;
    }

    /**
     * Compare the two float values.
     *
     * @param first   the first.
     * @param second  the second.
     * @return true if the values are equals.
     */
    public static boolean equals(float first, float second) {
        return Float.compare(first, second) == 0;
    }

    /**
     * Compare the two float values.
     *
     * @param first   the first.
     * @param second  the second.
     * @return true if the values are equals.
     */
    public static boolean equals(double first, double second) {
        return Double.compare(first, second) == 0;
    }

    /**
     * Compare the two float values by the epsilon.
     *
     * @param first   the first.
     * @param second  the second.
     * @param epsilon the epsilon.
     * @return true if the values are equals.
     */
    public static boolean equals(float first, float second, float epsilon) {
        return first == second || Math.abs(first - second) < epsilon;
    }

    /**
     * Compare the two float values by the epsilon.
     *
     * @param first   the first.
     * @param second  the second.
     * @param epsilon the epsilon.
     * @return true if the values are equals.
     */
    public static boolean equals(double first, double second, double epsilon) {
        return first == second || Math.abs(first - second) < epsilon;
    }

    /**
     * Compare the two float values by the epsilon.
     *
     * @param first   the first.
     * @param second  the second.
     * @param epsilon the epsilon.
     * @return true if the first value is less than the second value.
     */
    public static boolean lessThan(float first, float second, float epsilon) {
        return second - first > epsilon;
    }

    /**
     * Compare the two float values by the epsilon.
     *
     * @param first   the first.
     * @param second  the second.
     * @param epsilon the epsilon.
     * @return true if the first value is less than the second value.
     */
    public static boolean lessThan(double first, double second, double epsilon) {
        return second - first > epsilon;
    }

    /**
     * Compare the two float values by the epsilon.
     *
     * @param first   the first.
     * @param second  the second.
     * @param epsilon the epsilon.
     * @return true if the first value is greater than the second value.
     */
    public static boolean greaterThan(float first, float second, float epsilon) {
        return first - second > epsilon;
    }

    /**
     * Compare the two float values by the epsilon.
     *
     * @param first   the first.
     * @param second  the second.
     * @param epsilon the epsilon.
     * @return true if the first value is greater than the second value.
     */
    public static boolean greaterThan(double first, double second, double epsilon) {
        return first - second > epsilon;
    }

    /**
     * Cut the second part of the float value by the mod. For example: cut(1.123456F, 3) returns 1.123F.
     *
     * @param value the value.
     * @param mod   the mod.
     * @return the cut value.
     */
    public static float cut(float value, float mod) {
        return (int) (value * mod) / mod;
    }

    /**
     * Return zero if the value is null.
     *
     * @param value the value.
     * @return zero if the value is null.
     */
    public static Float zeroIfNull(Float value) {
        return value == null ? ZERO_FLOAT : value;
    }

    /**
     * Return zero if the value is null.
     *
     * @param value the value.
     * @return zero if the value is null.
     */
    public static Double zeroIfNull(Double value) {
        return value == null ? ZERO_DOUBLE : value;
    }

    /**
     * Return zero if the value is null.
     *
     * @param value the value.
     * @return zero if the value is null.
     */
    public static Integer zeroIfNull(Integer value) {
        return value == null ? ZERO_INTEGER : value;
    }

    /**
     * Returns zero if the value is null.
     *
     * @param value the value.
     * @return zero if the value is null.
     */
    public static Long zeroIfNull(Long value) {
        return value == null ? ZERO_LONG : value;
    }

    /**
     * Clamp the value between the min and the max.
     *
     * @param value the source value.
     * @param min the minimal value.
     * @param max the maximal value.
     * @return the clamped value.
     */
    public static float clamp(float value, float min, float max) {
        return value > max ? max : value < min ? min : value;
    }

    /**
     * Clamp the value between 0 and 1.
     *
     * @param value the source value.
     * @return the clamped value.
     */
    public static float clamp(float value) {
        return clamp(value, 0f, 1f);
    }

    /**
     * Linear time-based interpolation.
     *
     * @param min  the minimal value.
     * @param max  the maximal value.
     * @param time the time.
     * @return the interpolated value or maximal value if the time is greater than 1.0.
     */
    public static float lerp(float min, float max, float time) {
        if (time > 1.0f) {
            return max;
        }

        if (time < 0.0f) {
            return min;
        }

        return lerpUnnormal(min, max, time);
    }

    /**
     * Linear time-based interpolation.
     *
     * @param min  the minimal value.
     * @param max  the maximal value.
     * @param time the time.
     * @return the interpolated value.
     */
    public static float lerpUnnormal(float min, float max, float time) {
        return (1.f - time) * min + time * max;
    }

    /**
     * Return true if these vectors are equals.
     *
     * @param first   the first vector.
     * @param second  the second vector.
     * @param epsilon the epsilon.
     * @return true if these vectors are equals.
     */
    public static boolean isEquals(Vector3f first, Vector3f second, float epsilon) {
        if (first == null || second == null) {
            return false;
        } else {
            return first.equals(second, epsilon);
        }
    }

    /**
     * Converts an angle measured in degrees to an approximately equivalent angle measured in radians.
     *
     * @param degree angle in degrees
     * @return  the measurement of the angle in radians
     */
    public static float toRad(float degree) {
        return degree * DEG_TO_RAD;
    }

    /**
     * Converts an angle measured in radians to an approximately equivalent angle measured in degrees.
     *
     * @param rad angle in radians
     * @return the measurement of the angle in degrees
     */
    public static float toDeg(float rad) {
        return rad * RAD_TO_DEG;
    }

}

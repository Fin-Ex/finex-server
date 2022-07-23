package ru.finex.core.rng;

import java.util.Collection;
import java.util.List;

/**
 * Extender of default {@link java.util.random.RandomGenerator RandomGenerator}.
 *
 * @author m0nster.mind
 */
public interface RandomGenerator extends java.util.random.RandomGenerator {

    /**
     * Returns a pseudorandomly chosen element from list.
     * @param list list
     * @param <E> element type
     * @return returns a pseudorandomly chosen element from list, or null if list is empty.
     */
    default <E> E nextElement(List<E> list) {
        if (list.isEmpty()) {
            return null;
        }

        return list.get(nextInt(list.size()));
    }

    /**
     * Returns a pseudorandomly chosen element from elements array.
     * @param elements array of elements
     * @param <E> element type
     * @return returns a pseudorandomly chosen element from elements array or null if array of elements is empty.
     */
    default <E> E nextElement(E[] elements) {
        if (elements.length == 0) {
            return null;
        }

        return elements[nextInt(elements.length)];
    }

    /**
     * Returns a pseudorandomly chosen element from elements array.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen element from elements array.
     */
    default byte nextElement(byte[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return elements[nextInt(elements.length)];
    }

    /**
     * Returns a pseudorandomly chosen element from elements array.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen element from elements array.
     */
    default char nextElement(char[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return elements[nextInt(elements.length)];
    }

    /**
     * Returns a pseudorandomly chosen element from elements array.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen element from elements array.
     */
    default short nextElement(short[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return elements[nextInt(elements.length)];
    }

    /**
     * Returns a pseudorandomly chosen element from elements array.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen element from elements array.
     */
    default int nextElement(int[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return elements[nextInt(elements.length)];
    }

    /**
     * Returns a pseudorandomly chosen element from elements array.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen element from elements array.
     */
    default long nextElement(long[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return elements[nextInt(elements.length)];
    }

    /**
     * Returns a pseudorandomly chosen element from elements array.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen element from elements array.
     */
    default float nextElement(float[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return elements[nextInt(elements.length)];
    }

    /**
     * Returns a pseudorandomly chosen element from elements array.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen element from elements array.
     */
    default double nextElement(double[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return elements[nextInt(elements.length)];
    }

    /**
     * Returns a pseudorandomly chosen element from elements array.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen element from elements array.
     */
    default boolean nextElement(boolean[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return elements[nextInt(elements.length)];
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and collection size.
     * @param collection collection
     * @return returns a pseudorandomly chosen {@code int} between zero and collection size.
     */
    default int nextIndex(Collection<?> collection) {
        if (collection.isEmpty()) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(collection.size());
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     * @param elements array of elements
     * @param <E> element type
     * @return returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     */
    default <E> int nextIndex(E[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(elements.length);
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     */
    default int nextIndex(byte[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(elements.length);
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     */
    default int nextIndex(char[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(elements.length);
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     */
    default int nextIndex(short[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(elements.length);
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     */
    default int nextIndex(int[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(elements.length);
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     */
    default int nextIndex(long[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(elements.length);
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     */
    default int nextIndex(float[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(elements.length);
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     */
    default int nextIndex(double[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(elements.length);
    }

    /**
     * Returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     * @param elements array of elements
     * @return returns a pseudorandomly chosen {@code int} between zero and array of elements size.
     */
    default int nextIndex(boolean[] elements) {
        if (elements.length == 0) {
            throw new IllegalArgumentException("Elements is empty!");
        }

        return nextInt(elements.length);
    }

    /**
     * Unwrap random generator.
     * @return random generator
     */
    java.util.random.RandomGenerator unwrap();

}

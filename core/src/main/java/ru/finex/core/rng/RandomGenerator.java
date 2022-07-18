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
     * @return returns a pseudorandomly chosen element from list.
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
     * @return returns a pseudorandomly chosen element from elements array.
     */
    default <E> E nextElement(E[] elements) {
        if (elements.length == 0) {
            return null;
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
            return 0;
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
            return 0;
        }

        return nextInt(elements.length);
    }

    /**
     * Unwrap random generator.
     * @return random generator
     */
    java.util.random.RandomGenerator unwrap();

}

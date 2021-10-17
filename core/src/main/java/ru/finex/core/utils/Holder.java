package ru.finex.core.utils;

/**
 * @author m0nster.mind
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface Holder<T> {

    void set(T value);

    T get();

}

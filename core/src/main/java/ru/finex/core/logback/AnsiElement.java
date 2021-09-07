package ru.finex.core.logback;

/**
 * An ANSI encodable element.
 *
 * @author Phillip Webb
 * @since 1.0.0
 */
public interface AnsiElement {

    /**
     * @return the ANSI escape code
     */
    @Override
    String toString();

}

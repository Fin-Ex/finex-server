package ru.finex.core.placeholder;

/**
 * @author m0nster.mind
 */
public interface PlaceholderService {

    /**
     * Evaluate expression.
     *
     * @param expression string expression
     * @param resultType expected type
     * @param <T> generic expected type
     * @return evaluated result
     */
    <T> T evaluate(String expression, Class<T> resultType);

}

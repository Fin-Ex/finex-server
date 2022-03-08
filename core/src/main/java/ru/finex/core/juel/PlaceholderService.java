package ru.finex.core.juel;

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

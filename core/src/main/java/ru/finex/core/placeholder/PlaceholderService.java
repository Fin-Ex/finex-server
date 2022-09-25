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
    default <T> T evaluate(String expression, Class<T> resultType) {
        return evaluate(expression, null, resultType);
    }

    /**
     * Evaluate expression with caller context.
     *
     * @param expression string expression
     * @param caller caller
     * @param resultType expected type
     * @param <T> generic expected type
     * @return evaluated result
     */
    <T> T evaluate(String expression, Object caller, Class<T> resultType);

}

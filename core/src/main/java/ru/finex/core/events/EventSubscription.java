package ru.finex.core.events;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author m0nster.mind
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface EventSubscription<T> {

    EventSubscription<T> filter(Predicate<T> predicate);

    <O> EventSubscription<O> map(Function<T, O> function);

    EventSubscription<T> forEach(Consumer<T> consumer);

    /**
     * Short notation filter & map to specified type.
     * <code><pre>
     * subscription
     *     .filter(e -> e instanceof Type)
     *     .map(e -> (Type)e)
     * </pre></code>
     *
     * @param type type
     * @param <O> output
     * @return casted subscription
     */
    <O> EventSubscription<O> cast(Class<O> type);

}

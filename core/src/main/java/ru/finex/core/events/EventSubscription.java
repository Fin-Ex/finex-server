package ru.finex.core.events;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author m0nster.mind
 */
public interface EventSubscription<T> {

    EventSubscription<T> filter(Predicate<T> predicate);
    <Output> EventSubscription<Output> map(Function<T, Output> function);
    EventSubscription<T> forEach(Consumer<T> consumer);

    /**
     * Short notation filter & map to specified type.
     * <code><pre>
     * subscription
     *     .filter(e -> e instanceof Type)
     *     .map(e -> (Type)e)
     * </pre></code>
     */
    <Output> EventSubscription<Output> cast(Class<Output> type);

}

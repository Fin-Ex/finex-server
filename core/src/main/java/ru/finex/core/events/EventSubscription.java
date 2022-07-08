package ru.finex.core.events;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Репрезентация подписки на событие.
 *
 * @author m0nster.mind
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface EventSubscription<T> {

    /**
     * Фильтрация событий.
     *
     * @param predicate условие
     * @return this
     */
    EventSubscription<T> filter(Predicate<T> predicate);

    /**
     * Трансформация события из одного объекта в другой.
     *
     * @param function функция трансформации
     * @param <O> возвращаемый тип
     * @return this
     */
    <O> EventSubscription<O> map(Function<T, O> function);

    /**
     * Исполнение {@link Consumer} для каждого события.
     *
     * @param consumer операция применяемая для объекта
     * @return this
     */
    EventSubscription<T> forEach(Consumer<T> consumer);

    /**
     * Short notation filter and map to specified type.
     * <pre>{@code
     * subscription
     *     .filter(e -> e instanceof Type)
     *     .map(e -> (Type)e)
     * }</pre>
     *
     * @param type type
     * @param <O> output
     * @return casted subscription
     */
    <O> EventSubscription<O> cast(Class<O> type);

}

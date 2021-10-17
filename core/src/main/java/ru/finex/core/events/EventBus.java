package ru.finex.core.events;

/**
 * @author m0nster.mind
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface EventBus<E> {

    void notify(E object);

    EventSubscription<E> subscribe();

    void unsubscribe(EventSubscription<E> subscription);

    void unsubscribeAll();

}

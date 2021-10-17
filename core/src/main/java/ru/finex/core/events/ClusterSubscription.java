package ru.finex.core.events;

import java.util.function.Consumer;

/**
 * @author m0nster.mind
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface ClusterSubscription<T> {

    default void processEvents(Consumer<T> consumer) {
        processEvents(consumer, Integer.MAX_VALUE);
    }

    void processEvents(Consumer<T> consumer, int maxEvents);

}

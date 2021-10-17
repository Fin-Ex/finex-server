package ru.finex.core.events.local;

import ru.finex.core.events.EventBus;
import ru.finex.core.events.EventSubscription;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @param <E> event type
 * @author m0nster.mind
 */
public class LocalEventBus<E> implements EventBus<E> {

    private final Set<AbstractEventSubscription<?>> subscriptions = ConcurrentHashMap.newKeySet();

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void notify(E object) {
        for (AbstractEventSubscription subscription : subscriptions) {
            subscription.execute(object);
        }
    }

    @Override
    public EventSubscription<E> subscribe() {
        final SingleEventSubscription<E> subscription = new SingleEventSubscription<>();
        subscriptions.add(subscription);
        return subscription;
    }

    @Override
    public void unsubscribe(EventSubscription<E> subscription) {
        subscriptions.remove(subscription);
    }

    @Override
    public void unsubscribeAll() {
        subscriptions.clear();
    }

}

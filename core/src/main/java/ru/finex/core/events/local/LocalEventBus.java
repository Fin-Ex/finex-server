package ru.finex.core.events.local;

import ru.finex.core.events.EventBus;
import ru.finex.core.events.EventSubscription;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author m0nster.mind
 * @date 23.03.2018
 */
public class LocalEventBus<E> implements EventBus<E> {

	private final Set<AbstractEventSubscription<?>> subscriptions = ConcurrentHashMap.newKeySet();

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void notify(E object) {
		for (AbstractEventSubscription subscription : subscriptions) {
			subscription.execute(object);
		}
	}

	public EventSubscription<E> subscribe() {
		final SingleEventSubscription<E> subscription = new SingleEventSubscription<>();
		subscriptions.add(subscription);
		return subscription;
	}

	public void unsubscribe(EventSubscription<E> subscription) {
		subscriptions.remove(subscription);
	}

	public void unsubscribeAll() {
		subscriptions.clear();
	}

}

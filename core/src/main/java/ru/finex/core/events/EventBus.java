package ru.finex.core.events;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author m0nster.mind
 * @date 23.03.2018
 */
public class EventBus {

	private final Set<AbstractEventSubscription<?>> subscriptions = ConcurrentHashMap.newKeySet();

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void notify(Object object) {
		for (AbstractEventSubscription subscription : subscriptions) {
			subscription.execute(object);
		}
	}

	public <T> AbstractEventSubscription<T> subscribe() {
		final SingleEventSubscription<T> subscription = new SingleEventSubscription<>();
		subscriptions.add(subscription);
		return subscription;
	}

	public <T> void unsubscribe(AbstractEventSubscription<T> subscription) {
		subscriptions.remove(subscription);
	}

	public <T> void unsubscribeAll() {
		subscriptions.clear();
	}

}

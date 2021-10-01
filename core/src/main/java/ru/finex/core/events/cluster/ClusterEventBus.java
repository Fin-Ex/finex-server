package ru.finex.core.events.cluster;

import org.redisson.api.RTopic;
import ru.finex.core.events.ClusterSubscription;
import ru.finex.core.utils.GenericUtils;

/**
 * @author m0nster.mind
 */
public class ClusterEventBus<E> {

    private final RTopic topic;
    private final Class<E> genericType;

    public ClusterEventBus(RTopic topic) {
        this.topic = topic;
        genericType = GenericUtils.getGenericType(getClass(), 0);
    }

    public ClusterEventBus(RTopic topic, Class<E> genericType) {
        this.topic = topic;
        this.genericType = genericType;
    }

    public void notify(E object) {
        topic.publishAsync(object);
    }

    public ClusterSubscription<E> subscribe() {
        ClusterSubscriptionImpl<E> subscription = new ClusterSubscriptionImpl<>();
        int id = topic.addListener(genericType, subscription);
        subscription.setId(id);

        return subscription;
    }

    public void unsubscribe(ClusterSubscription<E> subscription) {
        topic.removeListener(((ClusterSubscriptionImpl<E>) subscription).getId());
    }

    public void unsubscribeAll() {
        topic.removeAllListeners();
    }
}

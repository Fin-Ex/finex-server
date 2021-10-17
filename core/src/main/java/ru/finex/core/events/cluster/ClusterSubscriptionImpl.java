package ru.finex.core.events.cluster;

import lombok.Getter;
import lombok.Setter;
import org.redisson.api.listener.MessageListener;
import ru.finex.core.events.ClusterSubscription;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

/**
 * @author m0nster.mind
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public class ClusterSubscriptionImpl<T> implements ClusterSubscription<T>, MessageListener<T> {

    private final Queue<T> queue = new ConcurrentLinkedQueue<>();
    @Getter
    @Setter
    private int id;

    @Override
    public void processEvents(Consumer<T> consumer, int maxEvents) {
        T event = queue.poll();
        for (int i = 0; event != null && i < maxEvents; event = queue.poll(), i++) {
            consumer.accept(event);
        }
    }

    @Override
    public void onMessage(CharSequence channel, T msg) {
        queue.offer(msg);
    }

    public void clear() {
        queue.clear();
    }

}

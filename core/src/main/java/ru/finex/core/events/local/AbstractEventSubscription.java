package ru.finex.core.events.local;

import ru.finex.core.events.EventPipe;
import ru.finex.core.events.EventSubscription;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @param <T> event message
 * @author m0nster.mind
 */
public abstract class AbstractEventSubscription<T> implements EventSubscription<T> {

    protected final List<EventPipe> pipe = new ArrayList<>();

    @Override
    public AbstractEventSubscription<T> filter(Predicate<T> predicate) {
        pipe.add(new PipeFilter<>(predicate));
        return this;
    }

    @Override
    public <O> AbstractEventSubscription<O> map(Function<T, O> function) {
        pipe.add(new PipeMap<>(function));
        return (AbstractEventSubscription<O>) this;
    }

    @Override
    public AbstractEventSubscription<T> forEach(Consumer<T> consumer) {
        pipe.add(new PipeConsumer<>(consumer));
        return this;
    }

    @Override
    public <O> AbstractEventSubscription<O> cast(Class<O> type) {
        pipe.add(new PipeFilter<>(o -> type.isAssignableFrom(o.getClass())));
        return (AbstractEventSubscription<O>) this;
    }

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    public abstract void execute(T object);

}

package ru.finex.core.events.local;

import ru.finex.core.events.EventPipe;
import ru.finex.core.events.EventSubscription;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 *
 * @author m0nster.mind
 * @date 23.03.2018
 */
public abstract class AbstractEventSubscription<T> implements EventSubscription<T> {

	protected final List<EventPipe> pipe = new ArrayList<>();

	@Override
	public AbstractEventSubscription<T> filter(Predicate<T> predicate) {
		pipe.add(new PipeFilter<>(predicate));
		return this;
	}

	@Override
	public <Output> AbstractEventSubscription<Output> map(Function<T, Output> function) {
		pipe.add(new PipeMap<>(function));
		return (AbstractEventSubscription<Output>) this;
	}

	@Override
	public AbstractEventSubscription<T> forEach(Consumer<T> consumer) {
		pipe.add(new PipeConsumer<>(consumer));
		return this;
	}

	@Override
	public <Output> AbstractEventSubscription<Output> cast(Class<Output> type) {
		pipe.add(new PipeFilter<>(o -> type.isAssignableFrom(o.getClass())));
		return (AbstractEventSubscription<Output>) this;
	}

	public abstract void execute(T object);

}

package ru.finex.core.events;

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
public abstract class AbstractEventSubscription<T> {

	protected final List<IEventPipe> pipe = new ArrayList<>();

	public AbstractEventSubscription<T> filter(Predicate<T> predicate) {
		pipe.add(new PipeFilter<>(predicate));
		return this;
	}

	public <Output> AbstractEventSubscription<Output> map(Function<T, Output> function) {
		pipe.add(new PipeMap<>(function));
		return (AbstractEventSubscription<Output>) this;
	}

	public AbstractEventSubscription<T> forEach(Consumer<T> consumer) {
		pipe.add(new PipeConsumer<>(consumer));
		return this;
	}

	/**
	 * Short notation filter & map to specified type. 	 <code><pre>
	 * subscription
	 *     .filter(e -> e instanceof Type)
	 *     .map(e -> (Type)e)
	 * </pre></code>
	 */
	public <Output> AbstractEventSubscription<Output> cast(Class<Output> type) {
		pipe.add(new PipeFilter<>(o -> type.isAssignableFrom(o.getClass())));
		return (AbstractEventSubscription<Output>) this;
	}

	abstract void execute(T object);

}

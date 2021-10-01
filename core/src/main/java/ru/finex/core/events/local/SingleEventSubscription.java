package ru.finex.core.events.local;

import ru.finex.core.events.EventPipe;

/**
 *
 * @author m0nster.mind
 * @date 23.03.2018
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class SingleEventSubscription<T> extends AbstractEventSubscription<T> {

	@Override
	public void execute(T object) {
		Object input = object;
		for (int i = 0; i < pipe.size() && input != null; i++) {
			final EventPipe pipe = this.pipe.get(i);
			input = pipe.process(input);
		}
	}

}

package ru.finex.core.events.local;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventPipe;

import java.util.function.Consumer;

/**
 *
 * @author m0nster.mind
 * @date 23.03.2018
 */
@RequiredArgsConstructor
public class PipeConsumer<Input> implements EventPipe<Input, Input> {

	private final Consumer<Input> consumer;

	@Override
	public Input process(Input object) {
		consumer.accept(object);
		return object;
	}

}

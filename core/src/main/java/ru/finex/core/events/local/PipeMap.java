package ru.finex.core.events.local;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventPipe;

import java.util.function.Function;

/**
 *
 * @author m0nster.mind
 * @date 23.03.2018
 */
@RequiredArgsConstructor
public class PipeMap<Input, Output> implements EventPipe<Input, Output> {

	private final Function<Input, Output> function;

	@Override
	public Output process(Input object) {
		return function.apply(object);
	}

}

package ru.finex.core.events.local;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventPipe;

import java.util.function.Predicate;

/**
 *
 * @author m0nster.mind
 * @date 23.03.2018
 */
@RequiredArgsConstructor
public class PipeFilter<Input> implements EventPipe<Input, Input> {

	private final Predicate<Input> predicate;

	@Override
	public Input process(Input object) {
		return predicate.test(object) ? object : null;
	}

}

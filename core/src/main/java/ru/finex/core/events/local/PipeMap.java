package ru.finex.core.events.local;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventPipe;

import java.util.function.Function;

/**
 * @param <I> input
 * @param <O> output
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class PipeMap<I, O> implements EventPipe<I, O> {

    private final Function<I, O> function;

    @Override
    public O process(I object) {
        return function.apply(object);
    }

}

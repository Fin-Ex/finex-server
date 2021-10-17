package ru.finex.core.events.local;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventPipe;

import java.util.function.Predicate;

/**
 * @param <I> input
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class PipeFilter<I> implements EventPipe<I, I> {

    private final Predicate<I> predicate;

    @Override
    public I process(I object) {
        return predicate.test(object) ? object : null;
    }

}

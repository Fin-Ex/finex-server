package ru.finex.core.events.local;

import lombok.RequiredArgsConstructor;
import ru.finex.core.events.EventPipe;

import java.util.function.Consumer;

/**
 * @param <I> input
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class PipeConsumer<I> implements EventPipe<I, I> {

    private final Consumer<I> consumer;

    @Override
    public I process(I object) {
        consumer.accept(object);
        return object;
    }

}

package ru.finex.core.events;

/**
 * @param <I> input
 * @param <O> output
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public interface EventPipe<I, O> {

    O process(I object);

}

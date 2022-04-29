package ru.finex.core.repository;

import lombok.RequiredArgsConstructor;
import ru.finex.core.command.AbstractCommandQueue;
import ru.finex.core.command.AbstractGameObjectCommand;
import ru.finex.core.command.CommandContext;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RequiredArgsConstructor
public class RepositoryFuture<T> implements Future<T> {

    private final AbstractCommandQueue<? extends AbstractGameObjectCommand, ? extends CommandContext> commandQueue;
    private final CompletableFuture<T> future;

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return future.cancel(mayInterruptIfRunning);
    }

    @Override
    public boolean isCancelled() {
        return future.isCancelled();
    }

    public boolean isDone() {
        return future.isDone();
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
        return future.get();
    }

    @Override
    public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return future.get(timeout, unit);
    }

//    public void addCallback(AbstractGameObjectCommand command, CommandContext context) {
//        future.thenRun(() -> commandQueue.offerCommand(command, context));
//    }

}

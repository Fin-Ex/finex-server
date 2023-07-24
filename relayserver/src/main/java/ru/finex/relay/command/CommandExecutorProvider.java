package ru.finex.relay.command;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * Provide executor to network queue.
 * <p/>
 * Create {@link ThreadPoolExecutor executor} and configure it from {@link CommandExecutorConfiguration configuration}.
 *
 * @author m0nster.mind
 * @see CommandExecutorConfiguration
 * @see RelayCommandQueue
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class CommandExecutorProvider implements Provider<Executor> {

    private final CommandExecutorConfiguration configuration;

    @Override
    public Executor get() {
        return new ThreadPoolExecutor(configuration.getMinimalThreads(), configuration.getMaximalThreads(),
            configuration.getKeepAlive(), TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>(),
            new ThreadFactoryBuilder()
                .setNameFormat("CommandExecutor-%d")
                .build()
        );
    }

}

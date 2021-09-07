package ru.finex.core.concurrent;

import lombok.extern.slf4j.Slf4j;

/**
 * @author m0nster.mind
 */
@Slf4j
public class RunnableServerTask extends ServerTask implements Runnable {

    private final Runnable runnable;

    public RunnableServerTask(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        try {
            runnable.run();
        } catch (Exception e) {
            log.error("Fail to execute task", e);
            throw new RuntimeException(e);
        }
    }
}

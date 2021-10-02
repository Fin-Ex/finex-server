package ru.finex.ws.concurrent.game;

import ru.finex.ws.tick.TickService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
public class GameTickExecutor {

    private static final long TICK_TIME = 100L;

    private final TickService tickService;
    private final Thread thread;

    @Inject
    public GameTickExecutor(TickService tickService) {
        this.tickService = tickService;
        thread = new GameWorkerThread(this::tickLoop);
        thread.start();
    }

    private void tickLoop() {
        for (;;) {
            tickService.tick();

            long deltaTimeMillis = tickService.getDeltaTimeMillis();
            if (deltaTimeMillis < TICK_TIME) {
                try {
                    Thread.sleep(TICK_TIME - deltaTimeMillis);
                } catch (InterruptedException e) {
                    Thread.interrupted();
                    break;
                }
            }
        }
    }

}

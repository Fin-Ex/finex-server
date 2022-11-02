package ru.finex.core;

import com.mycila.guice.ext.closeable.CloseableInjector;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Слушастель вызывающийся при получении сигнала {@code sigterm}.
 *
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SigtermListener implements Runnable {

    private final CloseableInjector injector;

    @Override
    public void run() {
        log.info("Sigterm signal received, shutdown server.");
        try {
            injector.close();
        } finally {
            GlobalContext.clear();
        }
    }

}

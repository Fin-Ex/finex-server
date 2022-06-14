package ru.finex.core;

import com.mycila.guice.ext.closeable.CloseableInjector;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Слушастель вызывающийся при получении сигнала {@code sigterm}.
 *
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = {@Inject})
public class SigtermListener implements Runnable {

    private final CloseableInjector injector;

    @Override
    public void run() {
        try {
            injector.close();
        } finally {
            GlobalContext.clear();
        }
    }

}

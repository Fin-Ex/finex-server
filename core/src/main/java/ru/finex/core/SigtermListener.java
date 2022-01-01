package ru.finex.core;

import com.mycila.guice.ext.closeable.CloseableInjector;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class SigtermListener implements Runnable {

    private final CloseableInjector injector;

    @Override
    public void run() {
        injector.close();
    }

}

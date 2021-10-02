package ru.finex.ws;

import com.google.inject.Injector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.ApplicationBuilt;
import ru.finex.core.ServerApplication;
import ru.finex.core.ServerContext;
import ru.finex.core.db.migration.Evolution;
import ru.finex.nif.SelectorThread;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@Evolution("ws")
public class WorldServerApplication implements ServerContext, ApplicationBuilt {

    @Getter @Setter
    private Injector injector;

    @Inject
    private SelectorThread selectorThread;

    public static void main(String[] args) {
        try {
            ServerApplication.start(WorldServerApplication.class.getPackageName(), args);
        } catch (Exception e) {
            log.error("Fail to start server", e);
            System.exit(-1);
        }
    }

    @Override
    public void onApplicationBuilt() {
        selectorThread.start();
    }

}

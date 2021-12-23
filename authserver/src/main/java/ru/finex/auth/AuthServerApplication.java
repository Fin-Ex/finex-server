package ru.finex.auth;

import com.google.inject.Injector;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import ru.finex.core.ApplicationBuilt;
import ru.finex.core.ServerApplication;
import ru.finex.evolution.Evolution;
import ru.finex.network.netty.service.NettyNetworkService;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
@Evolution("auth")
public class AuthServerApplication implements ApplicationBuilt {

    @Getter
    @Setter
    private Injector injector;

    @Inject
    @Named("ClientNetwork")
    private NettyNetworkService clientNetworkService;

    /**
     * Entry point.
     * @param args arguments
     */
    public static void main(String[] args) {
        try {
            ServerApplication.start(AuthServerApplication.class.getPackageName(), args);
        } catch (Exception e) {
            log.error("Fail to start server", e);
            System.exit(-1);
        }
    }

    @Override
    public void onApplicationBuilt() {
        clientNetworkService.bind();
    }

}

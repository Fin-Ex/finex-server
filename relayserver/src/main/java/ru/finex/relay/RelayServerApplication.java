package ru.finex.relay;

import lombok.extern.slf4j.Slf4j;
import ru.finex.core.ApplicationBuilt;
import ru.finex.core.ServerApplication;
import ru.finex.core.management.HawtioService;
import ru.finex.network.netty.service.NettyNetworkService;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Slf4j
@Singleton
public class RelayServerApplication implements ApplicationBuilt {

    @Inject
    private HawtioService hawtioService;

    @Inject
    @Named("ClientNetwork")
    private List<NettyNetworkService> clientNetworkServices;

    /**
     * Entry point.
     * @param args arguments
     */
    public static void main(String[] args) {
        try {
            ServerApplication.start(RelayServerApplication.class.getPackageName(), args);
        } catch (Exception e) {
            log.error("Fail to start server", e);
            System.exit(-1);
        }
    }

    @Override
    public void onApplicationBuilt() {
        hawtioService.deployHawtio();
        clientNetworkServices.forEach(NettyNetworkService::bind);
    }

}

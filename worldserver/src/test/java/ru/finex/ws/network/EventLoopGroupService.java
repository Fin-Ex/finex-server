package ru.finex.ws.network;

import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Getter;
import lombok.SneakyThrows;

import java.util.concurrent.TimeUnit;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
public class EventLoopGroupService {

    @Getter
    private final EventLoopGroup parentGroup;

    @Getter
    private final EventLoopGroup childGroup;

    @Inject
    public EventLoopGroupService(NetworkConfiguration configuration) {
        this.parentGroup = new NioEventLoopGroup(configuration.getAcceptorThreads());
        this.childGroup = new NioEventLoopGroup(configuration.getClientThreads());
    }

    @PreDestroy
    @SneakyThrows
    public void shutdown() {
        parentGroup.awaitTermination(15, TimeUnit.SECONDS);
        childGroup.awaitTermination(15, TimeUnit.SECONDS);
    }
}

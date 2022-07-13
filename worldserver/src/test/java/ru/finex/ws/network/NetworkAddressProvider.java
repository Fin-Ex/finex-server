package ru.finex.ws.network;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import javax.inject.Inject;
import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class NetworkAddressProvider implements Provider<InetSocketAddress> {

    private final NetworkConfiguration configuration;

    @SneakyThrows
    @Override
    public InetSocketAddress get() {
        return new InetSocketAddress(InetAddress.getByName(configuration.getHost()), configuration.getPort());
    }

}

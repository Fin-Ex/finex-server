package ru.finex.network.netty.service;

/**
 * @author m0nster.mind
 */
public interface NettyNetworkService {

    /**
     * Bind server socket.
     */
    void bind();

    /**
     * Asynchronous stop server channel with specified callback.
     * @param callback called when netty stopped channel
     * @see #shutdown()
     */
    void shutdown(Runnable callback);

    /**
     * Asynchronous stop server channel.
     * @see #shutdown(Runnable)
     */
    void shutdown();

    /**
     * Blocking stop server channel.
     * @throws InterruptedException stop task interrupted
     */
    void shutdownNow() throws InterruptedException;

}

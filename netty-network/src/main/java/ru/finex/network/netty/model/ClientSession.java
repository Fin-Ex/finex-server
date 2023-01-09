package ru.finex.network.netty.model;

import java.util.Map;

/**
 * @author m0nster.mind
 */
public interface ClientSession {

    /**
     * Return immutable client session context.
     * @return immutable session context
     */
    Map<Object, Object> getContext();

    /**
     * Put key-value into session context.
     * @param key key
     * @param value value
     */
    void putContext(Object key, Object value);

    /**
     * Determines is client detached.
     * @return true detached, otherwise false
     */
    boolean isDetached();

    /**
     * Send packet to channel.
     * @param dto packet to send
     */
    void sendPacket(NetworkDto dto);

    /**
     * Send packet and close channel.
     * @param dto packet to send
     */
    void close(NetworkDto dto);

    /**
     * Close client connection.
     * Ignores client traffic and packets.
     */
    void closeNow();

}

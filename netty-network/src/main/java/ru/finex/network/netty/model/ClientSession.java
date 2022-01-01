package ru.finex.network.netty.model;

/**
 * @author m0nster.mind
 */
public interface ClientSession {

    /**
     * Client login name.
     * @return login name
     */
    String getLogin();

    /**
     * Set client login name.
     * @param login login name
     */
    void setLogin(String login);

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

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
     * Close client connection.
     * Ignores client traffic and packets.
     */
    void closeNow();

}

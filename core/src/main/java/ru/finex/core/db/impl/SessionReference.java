package ru.finex.core.db.impl;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;

import java.util.Deque;
import java.util.LinkedList;

/**
 * @author m0nster.mind
 */
@Slf4j
@Data
public class SessionReference {

    private final Deque<Throwable> traces = new LinkedList<>();

    private Session session;
    private int referenceCount;
    private boolean isTransactional;

    public SessionReference(Session session, boolean isTransactional) {
        this.session = session;
        this.referenceCount = 1;
        this.isTransactional = isTransactional;

        //#if DEBUG
        traces.offerFirst(new Exception().fillInStackTrace());
        //#endif
    }

    /**
     * Decreases the reference count by 1 and close session if the reference count reaches at 0.
     * @return true if and only if the reference count became 0 and session has been closed
     */
    public boolean release() {
        boolean result = --referenceCount <= 0;
        if (result) {
            session.close();
        }

        //#if DEBUG
        traces.pollFirst();
        //#endif

        return result;
    }

    /**
     * Increases the reference count by 1.
     * @return session
     */
    public Session retain() {
        if (referenceCount <= 0) {
            throw new RuntimeException("Trying access to released session!");
        }
        referenceCount++;

        //#if DEBUG
        traces.offerLast(new Exception().fillInStackTrace());
        //#endif

        return session;
    }

    /**
     * Print into log stacktraces where this reference retain.
     */
    public void logAcquired() {
        for (Throwable trace : traces) {
            log.debug("{}, references {}", session.toString(), referenceCount, trace);
        }
    }

}

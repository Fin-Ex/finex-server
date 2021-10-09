package ru.finex.core.db.impl;

import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import ru.finex.core.GlobalContext;
import ru.finex.core.db.DbSessionService;

import java.util.ArrayDeque;
import javax.inject.Inject;

/**
 * Контекст для работы с БД в рамках транзакционности.
 *
 * @author m0nster.mind
 * @see javax.transaction.Transactional
 */
public class TransactionalContext {

    private final static ThreadLocal<TransactionalContext> local = new ThreadLocal<>();
    public static TransactionalContext get() {
        TransactionalContext ctx = local.get();
        if (ctx == null) {
            ctx = GlobalContext.injector.getInstance(TransactionalContext.class);
            local.set(ctx);
        }

        return ctx;
    }

    private final ArrayDeque<Session> sessionStack = new ArrayDeque<>();

    @Getter(AccessLevel.PACKAGE)
    private final SessionFactory sessionFactory;

    @Inject
    public TransactionalContext(DbSessionService sessionService) {
        sessionFactory = sessionService.getSessionFactory();
    }

    void putSession(Session session) {
        sessionStack.addFirst(session);
    }

    Session retrieveSession() {
        return sessionStack.pollFirst();
    }

    boolean hasSessions() {
        return !sessionStack.isEmpty();
    }

    /** Получение текущий сессии и начало/продолжение транзакции. */
    public Session session() {
        Session session = sessionStack.peekFirst();
        if (session == null) {
            session = sessionFactory.getCurrentSession();
            session.beginTransaction();
        }

        return session;
    }

    /**
     * Commit изменениях в рамках текущей транзакции.
     * Может не быть выполнено немедленно, если вызывающие методы помечены {@link javax.transaction.Transactional}.
     *
     * @param session сессия
     */
    public void commit(Session session) {
        if (sessionStack.isEmpty()) {
            session.getTransaction().commit();
        }
    }

    /**
     * Откат всех изменений сделанных в рамках текущей транзакции.
     * Может быть не выполнено немедленно, если вызывающие методы помеченны {@link javax.transaction.Transactional}.
     * @param session сессия
     */
    public void rollback(Session session) {
        Session savedSession = sessionStack.peekFirst();
        if (savedSession != null) {
            savedSession.getTransaction().setRollbackOnly();
        } else {
            session.getTransaction().rollback();
        }
    }

}

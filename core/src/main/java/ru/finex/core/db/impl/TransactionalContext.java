package ru.finex.core.db.impl;

import lombok.AccessLevel;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import ru.finex.core.GlobalContext;
import ru.finex.core.db.DbSessionService;

import java.util.ArrayDeque;
import java.util.Objects;
import javax.inject.Inject;

/**
 * Контекст для работы с БД в рамках транзакционности.
 *
 * @author m0nster.mind
 * @see jakarta.transaction.Transactional
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public class TransactionalContext {

    private static final ThreadLocal<TransactionalContext> LOCAL = new ThreadLocal<>();

    private final ArrayDeque<SessionReference> sessionStack = new ArrayDeque<>();

    @Getter(AccessLevel.PACKAGE)
    private final SessionFactory sessionFactory;

    @Inject
    public TransactionalContext(DbSessionService sessionService) {
        sessionFactory = sessionService.getSessionFactory();
    }

    public static TransactionalContext get() {
        TransactionalContext ctx = LOCAL.get();
        if (ctx == null) {
            ctx = GlobalContext.injector.getInstance(TransactionalContext.class);
            LOCAL.set(ctx);
        }

        return ctx;
    }

    public static void set(TransactionalContext ctx) {
        LOCAL.set(ctx);
    }

    /**
     * Получение текущий сессии и начало/продолжение транзакции.
     * @return {@link Session}
     */
    public Session session() {
        return session(false);
    }

    Session session(boolean isTransactional) {
        Session session;
        SessionReference reference = sessionStack.peekFirst();
        if (reference == null) {
            session = newSession(isTransactional);
        } else {
            session = reference.retain();
        }

        Transaction transaction = session.getTransaction();
        if (!transaction.isActive()) {
            session.beginTransaction();
        }

        return session;
    }

    boolean hasSessions() {
        return !sessionStack.isEmpty();
    }

    Session newSession(boolean isTransactional) {
        Session session = sessionFactory.openSession();
        sessionStack.offer(new SessionReference(session, isTransactional));
        session.beginTransaction();
        return session;
    }

    void close() {
        SessionReference reference = sessionStack.peekFirst();
        if (reference != null && reference.release()) {
            sessionStack.remove(reference);
        }
    }

    /**
     * Commit изменениях в рамках текущей транзакции.
     * Может не быть выполнено немедленно, если вызывающие методы помечены {@link jakarta.transaction.Transactional}.
     *
     */
    public void commit() {
        SessionReference reference = Objects.requireNonNull(sessionStack.peekFirst());
        if (!reference.isTransactional()) {
            Session session = reference.getSession();
            Transaction trx = session.getTransaction();
            if (trx != null && trx.getStatus() == TransactionStatus.ACTIVE) {
                trx.commit();
            }
        }

        close();
    }

    /**
     * Откат всех изменений сделанных в рамках текущей транзакции.
     * Может быть не выполнено немедленно, если вызывающие методы помеченны {@link jakarta.transaction.Transactional}.
     */
    public void rollback() {
        SessionReference reference = Objects.requireNonNull(sessionStack.peekFirst());
        Session session = reference.getSession();
        Transaction trx = session.getTransaction();

        if (trx.getStatus() == TransactionStatus.ACTIVE) {
            if (!reference.isTransactional()) {
                trx.rollback();
            } else {
                trx.setRollbackOnly();
            }
        }

        close();
    }

}

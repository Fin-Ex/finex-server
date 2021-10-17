package ru.finex.core.db.impl;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.stream.Stream;
import javax.persistence.TransactionRequiredException;
import javax.transaction.InvalidTransactionException;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;

/**
 * Прокси методов помеченных аннотацией {@link Transactional}.
 * Реализует начало транзакции перед исполнение метода, rollback при ошибках исполнения и
 *  commit при успешном выполнении.
 *
 * @author m0nster.mind
 * @see Transactional
 * @see TransactionalContext
 */
public class TransactionalMethodInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        TransactionalContext context = TransactionalContext.get();
        Transactional transactional = invocation.getMethod().getAnnotation(Transactional.class);
        TxType strategy = transactional.value();

        Session session;
        Transaction trx;

        if (strategy == TxType.REQUIRED || strategy == TxType.SUPPORTS) {
            session = context.session();
            trx = session.getTransaction();
        } else if (strategy == TxType.MANDATORY) {
            if (!context.hasSessions()) {
                throw new TransactionRequiredException("Exists transaction not found!");
            }

            session = context.session();
            trx = session.getTransaction();
        } else if (strategy == TxType.REQUIRES_NEW || strategy == TxType.NOT_SUPPORTED) {
            session = context.getSessionFactory().openSession();
            trx = session.beginTransaction();
        } else if (strategy == TxType.NEVER) {
            if (context.hasSessions()) {
                throw new InvalidTransactionException("Transaction is exists for NEVER transaction type!");
            }

            session = context.getSessionFactory().openSession();
            trx = session.beginTransaction();
        } else {
            throw new InvalidTransactionException("Invalid transactional type: " + strategy);
        }
        context.putSession(session);

        Object result;
        try {
            result = invocation.proceed();
        } catch (Exception e) {
            if (canRollback(transactional, e)) {
                trx.rollback();
            }

            closeSession(context, strategy);
            throw e;
        }

        if (trx.getRollbackOnly()) {
            trx.rollback();
        } else {
            trx.commit();
        }

        closeSession(context, strategy);
        return result;
    }

    private static boolean canRollback(Transactional transactional, Exception e) {
        Class[] rollbackOn = transactional.rollbackOn();
        return Stream.of(transactional.dontRollbackOn()).noneMatch(exception -> exception.isInstance(e)) &&
            (Stream.of(rollbackOn).anyMatch(exception -> exception.isInstance(e)) || rollbackOn.length == 0);
    }

    private static void closeSession(TransactionalContext context, TxType strategy) {
        Session session = context.retrieveSession();
        if (strategy == TxType.REQUIRES_NEW || strategy == TxType.NOT_SUPPORTED) {
            session.close();
        }
    }

}

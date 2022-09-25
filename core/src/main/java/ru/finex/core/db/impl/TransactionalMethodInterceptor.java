package ru.finex.core.db.impl;

import jakarta.transaction.InvalidTransactionException;
import jakarta.transaction.TransactionRequiredException;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;

import java.util.stream.Stream;

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

        Session session = session(context, strategy);
        Transaction trx = session.getTransaction();
        Object result;
        try {
            try {
                result = invocation.proceed();
            } catch (Exception e) {
                if (canRollback(transactional, e)) {
                    trx.rollback();
                }

                context.close();
                throw e;
            }

            commitOrRollback(trx);
        } finally {
            context.close();
        }

        return result;
    }

    private static Session session(TransactionalContext context, TxType strategy)
        throws InvalidTransactionException, TransactionRequiredException {
        Session session;
        if (strategy == TxType.REQUIRED || strategy == TxType.SUPPORTS) {
            session = context.session(true);
        } else if (strategy == TxType.MANDATORY) {
            if (!context.hasSessions()) {
                throw new TransactionRequiredException("Exists transaction not found!");
            }

            session = context.session(true);
        } else if (strategy == TxType.REQUIRES_NEW || strategy == TxType.NOT_SUPPORTED) {
            session = context.newSession(true);
        } else if (strategy == TxType.NEVER) {
            if (context.hasSessions()) {
                throw new InvalidTransactionException("Transaction is exists for NEVER transaction type!");
            }

            session = context.newSession(true);
        } else {
            throw new InvalidTransactionException("Invalid transactional type: " + strategy);
        }

        return session;
    }

    private static boolean canRollback(Transactional transactional, Exception e) {
        Class[] rollbackOn = transactional.rollbackOn();
        return Stream.of(transactional.dontRollbackOn()).noneMatch(exception -> exception.isInstance(e)) &&
            (Stream.of(rollbackOn).anyMatch(exception -> exception.isInstance(e)) || rollbackOn.length == 0);
    }

    private static void commitOrRollback(Transaction trx) {
        if (trx.getStatus() != TransactionStatus.ACTIVE) {
            return;
        }

        if (trx.getRollbackOnly()) {
            trx.rollback();
        } else {
            trx.commit();
        }
    }

}

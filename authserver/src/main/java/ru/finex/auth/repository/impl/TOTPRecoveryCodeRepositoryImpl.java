package ru.finex.auth.repository.impl;

import org.hibernate.Session;
import ru.finex.auth.model.entity.TOTPRecoveryCodeEntity;
import ru.finex.auth.repository.TOTPRecoveryCodeRepository;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.repository.AbstractCrudRepository;

import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:Indentation")
@Singleton
public class TOTPRecoveryCodeRepositoryImpl extends AbstractCrudRepository<TOTPRecoveryCodeEntity, Long> implements TOTPRecoveryCodeRepository {

    @Override
    public boolean deleteByUserIdAndCode(Long userId, String code) {
        String query = """
            delete from TOTPRecoveryCodeEntity
            where userId = :userId
              and code = :code
        """;

        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            int count = session.createQuery(query)
                .setParameter("userId", userId)
                .setParameter("code", code)
                .executeUpdate();

            ctx.commit(session);
            return count != 0;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteByUserId(Long userId) {
        String query = """
            delete from TOTPRecoveryCodeEntity
            where userId = :userId
        """;

        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            int count = session.createQuery(query)
                .setParameter("userId", userId)
                .executeUpdate();

            ctx.commit(session);
            return count;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

}

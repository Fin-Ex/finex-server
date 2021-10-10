package ru.finex.auth.repository.impl;

import org.hibernate.Session;
import ru.finex.auth.model.entity.UserEntity;
import ru.finex.auth.repository.UserRepository;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.repository.AbstractCrudRepository;

import javax.inject.Singleton;
import javax.persistence.NoResultException;

/**
 * @author m0nster.mind
 */
@Singleton
public class UserRepositoryImpl extends AbstractCrudRepository<UserEntity, Long> implements UserRepository {

    @Override
    public UserEntity findByLogin(String login) {
        String query = """
            select user
            from UserEntity user
            where user.login = :login
        """;

        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            UserEntity entity = session.createQuery(query, entityClass)
                .setParameter("login", login)
                .getSingleResult();
            ctx.commit(session);
            return entity;
        } catch (NoResultException e) {
            return null;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void clearSecret(Long id) {
        String query = """
            update UserEntity
            set secret = null
            where userId = :userId
        """;

        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            session.createQuery(query)
                .setParameter("userId", id)
                .executeUpdate();
            ctx.commit(session);
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

}

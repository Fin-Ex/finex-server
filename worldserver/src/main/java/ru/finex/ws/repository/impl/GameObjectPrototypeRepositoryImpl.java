package ru.finex.ws.repository.impl;

import org.hibernate.Session;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.repository.AbstractCrudRepository;
import ru.finex.ws.model.entity.GameObjectPrototype;
import ru.finex.ws.repository.GameObjectPrototypeRepository;

import javax.inject.Singleton;
import javax.persistence.Query;

/**
 * @author finfan
 */
@Singleton
public class GameObjectPrototypeRepositoryImpl
    extends AbstractCrudRepository<GameObjectPrototype, Integer>
    implements GameObjectPrototypeRepository {

    @Override
    public GameObjectPrototype findByName(String name) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            Query query = session.createQuery("SELECT t FROM GameObjectTemplate t WHERE name = :name");
            query.setParameter("name", name);
            Object singleResult = query.getSingleResult();
            ctx.commit(session);
            return (GameObjectPrototype) singleResult;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }
}

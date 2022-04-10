package ru.finex.ws.repository.impl;

import org.hibernate.Session;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.repository.AbstractCrudRepository;
import ru.finex.core.repository.RepositoryFuture;
import ru.finex.ws.model.entity.GameObjectComponentPrototype;
import ru.finex.ws.repository.GameObjectComponentPrototypeRepository;

import java.util.List;
import javax.inject.Singleton;
import javax.persistence.Query;

/**
 * @author finfan
 */
@Singleton
public class GameObjectComponentPrototypeRepositoryImpl
    extends AbstractCrudRepository<GameObjectComponentPrototype, Integer>
    implements GameObjectComponentPrototypeRepository {

    @Override
    public RepositoryFuture<List<GameObjectComponentPrototype>> findByPrototypeNameAsync(String prototypeName) {
        return asyncOperation(() -> findByPrototypeName(prototypeName));
    }

    @Override
    public List<GameObjectComponentPrototype> findByPrototypeName(String prototypeName) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            Query query = session.createQuery("""
                    SELECT component 
                    FROM GameObjectComponentPrototype component 
                    JOIN component.gameObjectPrototype 
                    WHERE component.gameObjectPrototype.name = :prototypeName
                    """);
            query.setParameter("prototypeName", prototypeName);
            List<GameObjectComponentPrototype> entities = query.getResultList();
            ctx.commit(session);
            return entities;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }
}

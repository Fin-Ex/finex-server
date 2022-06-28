package ru.finex.ws.repository;

import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.repository.DefaultCrudRepository;
import ru.finex.ws.model.entity.GameObjectComponentPrototype;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author m0nster.mind
 */
public class GameObjectComponentPrototypeRepository extends DefaultCrudRepository<GameObjectComponentPrototype, Integer> {

    @Inject
    public GameObjectComponentPrototypeRepository(@Named("RepositoryExecutor") ExecutorService executorService) {
        this.executorService = executorService;
        entityClass = GameObjectComponentPrototype.class;
    }

    /**
     * Async find component prototypes by prototype name.
     * @param prototypeName prototype name
     * @return component prototypes entity
     */
    public Future<List<GameObjectComponentPrototype>> findPrototypesByPrototypeNameAsync(String prototypeName) {
        return asyncOperation(() -> findPrototypesByPrototypeName(prototypeName));
    }

    /**
     * Find a component prototypes by prototype name.
     * @param prototypeName prototype name
     * @return component prototypes entity
     */
    public List<GameObjectComponentPrototype> findPrototypesByPrototypeName(String prototypeName) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            Query<GameObjectComponentPrototype> query = session.createNamedQuery(getClass().getCanonicalName() + ".findPrototypesByPrototypeName", GameObjectComponentPrototype.class)
                .setParameter("prototypeName", prototypeName);
            return query.getResultList();
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }
}

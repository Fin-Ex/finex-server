package ru.finex.core.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.model.entity.Entity;
import ru.finex.core.utils.GenericUtils;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import javax.inject.Inject;
import javax.inject.Named;

/**
 * @param <T>  entity
 * @param <ID> id of entity
 * @author finfan
 */
@Slf4j
public abstract class AbstractCrudRepository<T extends Entity<ID>, ID extends Serializable> implements CrudRepository<T, ID> {

    protected final Class<T> entityClass = GenericUtils.getGenericType(getClass(), 0);

    @Inject
    @Named("Service")
    protected ExecutorService executorService;

    @Override
    public RepositoryFuture<T> createAsync(T entity) {
        return asyncOperation(() -> create(entity));
    }

    @Override
    public T create(T entity) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            ID persistenceId = (ID) session.save(entity);
            ctx.commit(session);
            entity.setPersistenceId(persistenceId);
            return entity;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RepositoryFuture<Void> updateAsync(T entity) {
        return asyncOperation(() -> update(entity));
    }

    @Override
    public Void update(T entity) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            session.update(entity);
            ctx.commit(session);
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public RepositoryFuture<T> restoreAsync(T entity) {
        return asyncOperation(() -> restore(entity));
    }

    @Override
    public T restore(T entity) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            session.load(entity, entity.getPersistenceId());
            ctx.commit(session);
            return entity;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RepositoryFuture<Void> deleteAsync(T entity) {
        return asyncOperation(() -> delete(entity));
    }

    @Override
    public Void delete(T entity) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            session.delete(entity);
            ctx.commit(session);
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public RepositoryFuture<List<T>> findAllAsync() {
        return asyncOperation(this::findAll);
    }

    @Override
    public List<T> findAll() {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            Query<T> query = session.createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t", entityClass);
            List<T> entities = query.getResultList();
            ctx.commit(session);
            return entities;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

    @Override
    public RepositoryFuture<T> findByIdAsync(ID id) {
        return asyncOperation(() -> findById(id));
    }

    @Override
    public T findById(ID id) {
        TransactionalContext ctx = TransactionalContext.get();
        Session session = ctx.session();
        try {
            T entity = session.find(entityClass, id);
            ctx.commit(session);
            return entity;
        } catch (Exception e) {
            ctx.rollback(session);
            throw new RuntimeException(e);
        }
    }

    protected <T> RepositoryFuture<T> asyncOperation(Supplier<T> operation) {
        return new RepositoryFuture<>(null, CompletableFuture.supplyAsync(operation, executorService));
    }

}

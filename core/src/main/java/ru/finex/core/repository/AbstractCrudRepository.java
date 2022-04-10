package ru.finex.core.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.query.Query;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.model.entity.Entity;
import ru.finex.core.utils.GenericUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * @param <T>  entity
 * @param <ID> id of entity
 * @author finfan
 */
@Slf4j
public abstract class AbstractCrudRepository<T extends Entity<ID>, ID extends Serializable> implements CrudRepository<T, ID> {

    protected final Class<T> entityClass = GenericUtils.getGenericType(getClass(), 0);

    @Inject
    @Named("commandExecutor")
    protected ExecutorService executorService;

    @Override
    public RepositoryFuture<T> create(T entity) {
        return (RepositoryFuture<T>) executorService.submit(() -> createInternal(entity));
    }

    public T createInternal(T entity) {
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
    public RepositoryFuture<Void> update(T entity) {
        return (RepositoryFuture<Void>) executorService.submit(() -> updateInternal(entity));
    }

    public Void updateInternal(T entity) {
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
    public RepositoryFuture<T> restore(T entity) {
        return (RepositoryFuture<T>) executorService.submit(() -> restoreInternal(entity));
    }

    public T restoreInternal(T entity) {
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
    public RepositoryFuture<Void> delete(T entity) {
        return (RepositoryFuture<Void>) executorService.submit(() -> deleteInternal(entity));
    }

    public Void deleteInternal(T entity) {
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

    public RepositoryFuture<List<T>> findAll() {
        return (RepositoryFuture<List<T>>) executorService.submit(() -> findAllInternal());
    }

    public List<T> findAllInternal() {
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
    public RepositoryFuture<T> findById(ID id) {
        return (RepositoryFuture<T>) executorService.submit(() -> findByIdInternal(id));
    }

    public T findByIdInternal(ID id) {
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

}

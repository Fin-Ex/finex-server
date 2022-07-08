package ru.finex.core.repository;

import ru.finex.core.model.entity.EntityObject;

import java.io.Serializable;
import java.util.List;

/**
 * @author finfan: 14.09.2021
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface CrudRepository<T extends EntityObject<ID>, ID extends Serializable> {

    RepositoryFuture<T> createAsync(T entity);

    T create(T entity);

    RepositoryFuture<T> restoreAsync(T entity);

    T restore(T entity);

    RepositoryFuture<Void> updateAsync(T entity);

    Void update(T entity);

    RepositoryFuture<Void> deleteAsync(T entity);

    Void delete(T entity);

    RepositoryFuture<List<T>> findAllAsync();

    List<T> findAll();

    RepositoryFuture<T> findByIdAsync(ID id);

    T findById(ID id);

}

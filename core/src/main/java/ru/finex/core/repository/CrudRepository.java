package ru.finex.core.repository;

import ru.finex.core.model.entity.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author finfan: 14.09.2021
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface CrudRepository<T extends Entity<ID>, ID extends Serializable> {

    RepositoryFuture<T> create(T entity);

    RepositoryFuture<T> restore(T entity);

    RepositoryFuture<Void> update(T entity);

    RepositoryFuture<Void> delete(T entity);

    RepositoryFuture<List<T>> findAll();

    RepositoryFuture<T> findById(ID id);

}

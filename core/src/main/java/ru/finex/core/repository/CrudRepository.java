package ru.finex.core.repository;

import ru.finex.core.model.entity.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * @author finfan: 14.09.2021
 */
@SuppressWarnings({"checkstyle:JavadocType", "checkstyle:MissingJavadocMethod"})
public interface CrudRepository<T extends Entity<ID>, ID extends Serializable> {

    FutureImpl<T> create(T entity);

    FutureImpl<T> restore(T entity);

    FutureImpl<Void> update(T entity);

    FutureImpl<Void> delete(T entity);

    FutureImpl<List<T>> findAll();

    FutureImpl<T> findById(ID id);

}

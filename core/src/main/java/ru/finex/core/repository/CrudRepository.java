package ru.finex.core.repository;

import ru.finex.core.model.entity.Entity;

import java.util.List;

/**
 *
 * @project finex-server
 * @author finfan: 14.09.2021
 */
public interface CrudRepository<T extends Entity> {
	T create(T entity);
	T restore(T entity);
	void update(T entity);
	void delete(T entity);
	List<T> findAll();
	T findById(int id);
}

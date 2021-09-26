package ru.finex.core.repository;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.finex.core.db.DbSessionService;
import ru.finex.core.model.entity.Entity;
import ru.finex.core.utils.ClassUtils;

import java.util.List;
import javax.inject.Inject;
import javax.persistence.Query;

/**
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
@Slf4j
public abstract class AbstractCrudRepository<T extends Entity> implements CrudRepository<T> {

	private final Class<T> entityClass = ClassUtils.getGenericType(getClass(), 0);

	@Inject
	protected DbSessionService sessionService;

	@Override
	public T create(T entity) {
		Transaction transaction = null;
		try(Session session = sessionService.openSession()) {
			transaction = session.beginTransaction();
			int persistenceId = (int) session.save(entity);
			entity.setPersistenceId(persistenceId);
			transaction.commit();
			return entity;
		} catch (Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public void update(T entity) {
		Transaction transaction = null;
		try(Session session = sessionService.openSession()) {
			transaction = session.beginTransaction();
			session.update(entity);
			transaction.commit();
		} catch (Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public T restore(T entity) {
		Transaction transaction = null;
		try(Session session = sessionService.openSession()) {
			transaction = session.beginTransaction();
			session.load(entity, entity.getPersistenceId());
			transaction.commit();
			return entity;
		} catch (Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public void delete(T entity) {
		Transaction transaction = null;
		try(Session session = sessionService.openSession()) {
			transaction = session.beginTransaction();
			session.delete(entity);
			transaction.commit();
		} catch (Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public List<T> findAll() {
		Transaction transaction = null;
		try(Session session = sessionService.openSession()) {
			transaction = session.beginTransaction();
			Query query = session.createQuery("SELECT t FROM " + entityClass.getSimpleName() + " t");
			List<T> entities = query.getResultList();
			transaction.commit();
			return entities;
		} catch (Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(e);
		}
	}

	@Override
	public T findById(int id) {
		Transaction transaction = null;
		try(Session session = sessionService.openSession()) {
			transaction = session.beginTransaction();
			T entity = session.find(entityClass, id);
			transaction.commit();
			return entity;
		} catch (Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(e);
		}
	}

}

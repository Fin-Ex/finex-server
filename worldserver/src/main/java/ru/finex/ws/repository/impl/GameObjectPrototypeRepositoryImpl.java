package ru.finex.ws.repository.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.finex.core.db.DbSessionService;
import ru.finex.core.repository.AbstractCrudRepository;
import ru.finex.ws.model.entity.GameObjectTemplate;
import ru.finex.ws.repository.GameObjectPrototypeRepository;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.Query;

/**
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
@Singleton
public class GameObjectPrototypeRepositoryImpl
	extends AbstractCrudRepository<GameObjectTemplate>
	implements GameObjectPrototypeRepository {

	@Inject
	private DbSessionService sessionService;

	@Override
	public GameObjectTemplate findByName(String name) {
		Transaction transaction = null;
		try(Session session = sessionService.openSession()) {
			transaction = session.beginTransaction();
			Query query = session.createQuery("SELECT t FROM GameObjectTemplate t WHERE name = :name");
			query.setParameter("name", name);
			Object singleResult = query.getSingleResult();
			transaction.commit();
			return (GameObjectTemplate) singleResult;
		} catch (Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(e);
		}
	}
}

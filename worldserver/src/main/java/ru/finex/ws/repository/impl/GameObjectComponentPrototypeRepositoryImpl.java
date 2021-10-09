package ru.finex.ws.repository.impl;

import org.hibernate.Session;
import ru.finex.core.db.impl.TransactionalContext;
import ru.finex.core.repository.AbstractCrudRepository;
import ru.finex.ws.model.entity.GameObjectComponentPrototype;
import ru.finex.ws.repository.GameObjectComponentPrototypeRepository;

import java.util.List;
import javax.inject.Singleton;
import javax.persistence.Query;

/**
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
@Singleton
public class GameObjectComponentPrototypeRepositoryImpl
	extends AbstractCrudRepository<GameObjectComponentPrototype, Integer>
	implements GameObjectComponentPrototypeRepository {

	@Override
	public List<GameObjectComponentPrototype> findByGameObjectTemplateName(String gameObjectTemplateName) {
		TransactionalContext ctx = TransactionalContext.get();
		Session session = ctx.session();
		try {
			Query query = session.createQuery(
				"SELECT component " +
				"FROM GameObjectTemplate template " +
				"JOIN GameObjectComponentTemplate component ON template.id = component.gameObjectTemplateId " +
				"WHERE template.name = :gameObjectTemplateName");
			query.setParameter("gameObjectTemplateName", gameObjectTemplateName);
			List<GameObjectComponentPrototype> entities = query.getResultList();
			ctx.commit(session);
			return entities;
		} catch (Exception e) {
			ctx.rollback(session);
			throw new RuntimeException(e);
		}
	}
}

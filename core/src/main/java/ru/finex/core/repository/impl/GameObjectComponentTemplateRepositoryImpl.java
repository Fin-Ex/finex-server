package ru.finex.core.repository.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.finex.core.repository.AbstractCrudRepository;
import ru.finex.core.repository.GameObjectComponentTemplateRepository;
import ru.finex.core.model.entity.GameObjectComponentTemplate;

import java.util.List;
import javax.persistence.Query;

/**
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
public class GameObjectComponentTemplateRepositoryImpl
	extends AbstractCrudRepository<GameObjectComponentTemplate>
	implements GameObjectComponentTemplateRepository {

	@Override
	public List<GameObjectComponentTemplate> findByGameObjectTemplateName(String gameObjectTemplateName) {
		Transaction transaction = null;
		try(Session session = sessionService.openSession()) {
			transaction = session.beginTransaction();
			Query query = session.createQuery(
				"SELECT component " +
				"FROM GameObjectTemplate template " +
				"JOIN GameObjectComponentTemplate component ON template.id = component.gameObjectTemplateId " +
				"WHERE template.name = :gameObjectTemplateName");
			query.setParameter("gameObjectTemplateName", gameObjectTemplateName);
			List<GameObjectComponentTemplate> entities = query.getResultList();
			transaction.commit();
			return entities;
		} catch (Exception e) {
			if(transaction != null) {
				transaction.rollback();
			}
			throw new RuntimeException(e);
		}
	}
}

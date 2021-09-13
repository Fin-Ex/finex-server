package ru.finex.core.repository;

import com.google.inject.ImplementedBy;
import ru.finex.core.repository.impl.GameObjectTemplateRepositoryImpl;
import ru.finex.core.templates.GameObjectTemplate;

/**
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
@ImplementedBy(GameObjectTemplateRepositoryImpl.class)
public interface GameObjectTemplateRepository extends CrudRepository<GameObjectTemplate> {
	GameObjectTemplate findByName(String name);
}

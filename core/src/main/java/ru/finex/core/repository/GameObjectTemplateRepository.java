package ru.finex.core.repository;

import com.google.inject.ImplementedBy;
import ru.finex.core.model.entity.GameObjectTemplate;
import ru.finex.core.repository.impl.GameObjectTemplateRepositoryImpl;

/**
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
@ImplementedBy(GameObjectTemplateRepositoryImpl.class)
public interface GameObjectTemplateRepository extends CrudRepository<GameObjectTemplate> {

	GameObjectTemplate findByName(String name);

}

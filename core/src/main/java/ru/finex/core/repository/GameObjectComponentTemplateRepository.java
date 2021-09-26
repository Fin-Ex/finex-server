package ru.finex.core.repository;

import com.google.inject.ImplementedBy;
import ru.finex.core.model.entity.GameObjectComponentTemplate;
import ru.finex.core.repository.impl.GameObjectComponentTemplateRepositoryImpl;

import java.util.List;

/**
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
@ImplementedBy(GameObjectComponentTemplateRepositoryImpl.class)
public interface GameObjectComponentTemplateRepository extends CrudRepository<GameObjectComponentTemplate> {

	List<GameObjectComponentTemplate> findByGameObjectTemplateName(String gameObjectTemplateName);

}

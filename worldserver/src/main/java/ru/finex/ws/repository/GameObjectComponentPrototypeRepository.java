package ru.finex.ws.repository;

import com.google.inject.ImplementedBy;
import ru.finex.core.repository.CrudRepository;
import ru.finex.ws.model.entity.GameObjectComponentTemplate;
import ru.finex.ws.repository.impl.GameObjectComponentPrototypeRepositoryImpl;

import java.util.List;

/**
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
@ImplementedBy(GameObjectComponentPrototypeRepositoryImpl.class)
public interface GameObjectComponentPrototypeRepository extends CrudRepository<GameObjectComponentTemplate> {

	List<GameObjectComponentTemplate> findByGameObjectTemplateName(String gameObjectTemplateName);

}

package ru.finex.ws.repository;

import com.google.inject.ImplementedBy;
import ru.finex.core.repository.CrudRepository;
import ru.finex.ws.model.entity.GameObjectPrototype;
import ru.finex.ws.repository.impl.GameObjectPrototypeRepositoryImpl;

/**
 *
 * @project finex-server
 * @author finfan: 13.09.2021
 */
@ImplementedBy(GameObjectPrototypeRepositoryImpl.class)
public interface GameObjectPrototypeRepository extends CrudRepository<GameObjectPrototype> {

	GameObjectPrototype findByName(String name);

}

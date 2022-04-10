package ru.finex.ws.repository;

import com.google.inject.ImplementedBy;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.RepositoryFuture;
import ru.finex.ws.model.entity.GameObjectPrototype;
import ru.finex.ws.repository.impl.GameObjectPrototypeRepositoryImpl;

/**
 * @author finfan
 */
@ImplementedBy(GameObjectPrototypeRepositoryImpl.class)
public interface GameObjectPrototypeRepository extends CrudRepository<GameObjectPrototype, Integer> {

    /**
     * Find game object prototype by name asynchronously.
     * @param name prototype name
     * @return future
     */
    RepositoryFuture<GameObjectPrototype> findByNameAsync(String name);

    /**
     * Find game object prototype by name.
     * @param name prototype name
     * @return prototype
     */
    GameObjectPrototype findByName(String name);

}

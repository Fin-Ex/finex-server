package ru.finex.ws.repository;

import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.NamedQuery;
import ru.finex.ws.model.entity.GameObjectPrototype;

import java.util.concurrent.Future;

/**
 * @author finfan
 */
public interface GameObjectPrototypeRepository extends CrudRepository<GameObjectPrototype, Integer> {

    /**
     * Find game object prototype by name asynchronously.
     * @param name prototype name
     * @return future
     */
    @NamedQuery
    Future<GameObjectPrototype> findByNameAsync(String name);

    /**
     * Find game object prototype by name.
     * @param name prototype name
     * @return prototype
     */
    @NamedQuery
    GameObjectPrototype findByName(String name);

}

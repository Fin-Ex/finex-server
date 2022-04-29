package ru.finex.ws.repository;

import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.NamedQuery;
import ru.finex.core.repository.RepositoryFuture;
import ru.finex.ws.model.entity.GameObjectPrototype;

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
    RepositoryFuture<GameObjectPrototype> findByNameAsync(String name);

    /**
     * Find game object prototype by name.
     * @param name prototype name
     * @return prototype
     */
    @NamedQuery
    GameObjectPrototype findByName(String name);

}

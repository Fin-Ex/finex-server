package ru.finex.ws.repository;

import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.NamedQuery;
import ru.finex.core.repository.RepositoryFuture;
import ru.finex.ws.model.entity.GameObjectComponentPrototype;

import java.util.List;

/**
 * @author finfan
 */
public interface GameObjectComponentPrototypeRepository extends CrudRepository<GameObjectComponentPrototype, Integer> {

    /**
     * Find all components by prototype name asynchronously.
     * @param prototypeName prototype name
     * @return future
     */
    @NamedQuery
    RepositoryFuture<List<GameObjectComponentPrototype>> findByPrototypeNameAsync(String prototypeName);

    /**
     * Find all components by prototype name.
     * @param prototypeName prototype name
     * @return components
     */
    @NamedQuery
    List<GameObjectComponentPrototype> findByPrototypeName(String prototypeName);

}

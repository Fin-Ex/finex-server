package ru.finex.ws.repository;

import com.google.inject.ImplementedBy;
import ru.finex.core.repository.CrudRepository;
import ru.finex.core.repository.RepositoryFuture;
import ru.finex.ws.model.entity.GameObjectComponentPrototype;
import ru.finex.ws.repository.impl.GameObjectComponentPrototypeRepositoryImpl;

import java.util.List;

/**
 * @author finfan
 */
@ImplementedBy(GameObjectComponentPrototypeRepositoryImpl.class)
public interface GameObjectComponentPrototypeRepository extends CrudRepository<GameObjectComponentPrototype, Integer> {

    /**
     * Find all components by prototype name asynchronously.
     * @param prototypeName prototype name
     * @return future
     */
    RepositoryFuture<List<GameObjectComponentPrototype>> findByPrototypeNameAsync(String prototypeName);

    /**
     * Find all components by prototype name.
     * @param prototypeName prototype name
     * @return components
     */
    List<GameObjectComponentPrototype> findByPrototypeName(String prototypeName);

}

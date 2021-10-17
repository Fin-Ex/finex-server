package ru.finex.ws.repository;

import com.google.inject.ImplementedBy;
import ru.finex.core.repository.CrudRepository;
import ru.finex.ws.model.entity.GameObjectPrototype;
import ru.finex.ws.repository.impl.GameObjectPrototypeRepositoryImpl;

/**
 * @author finfan
 */
@ImplementedBy(GameObjectPrototypeRepositoryImpl.class)
public interface GameObjectPrototypeRepository extends CrudRepository<GameObjectPrototype, Integer> {

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    GameObjectPrototype findByName(String name);

}

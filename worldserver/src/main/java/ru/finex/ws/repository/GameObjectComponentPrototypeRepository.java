package ru.finex.ws.repository;

import com.google.inject.ImplementedBy;
import ru.finex.core.repository.CrudRepository;
import ru.finex.ws.model.entity.GameObjectComponentPrototype;
import ru.finex.ws.repository.impl.GameObjectComponentPrototypeRepositoryImpl;

import java.util.List;

/**
 * @author finfan
 */
@ImplementedBy(GameObjectComponentPrototypeRepositoryImpl.class)
public interface GameObjectComponentPrototypeRepository extends CrudRepository<GameObjectComponentPrototype, Integer> {

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    List<GameObjectComponentPrototype> findByGameObjectTemplateName(String gameObjectTemplateName);

}

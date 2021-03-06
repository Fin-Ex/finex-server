package ru.finex.core.persistence;

import ru.finex.core.model.GameObject;
import ru.finex.core.model.entity.EntityObject;

/**
 * @author m0nster.mind
 */
@SuppressWarnings({"checkstyle:MissingJavadocMethod", "checkstyle:JavadocType"})
public interface PersistenceService<T extends EntityObject> {

    T persist(T entity);

    T restore(int gameObjectPersistenceId);

    default T restore(GameObject gameObject) {
        return restore(gameObject.getPersistenceId());
    }

}

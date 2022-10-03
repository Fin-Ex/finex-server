package ru.finex.core.persistence;

import ru.finex.core.model.GameObject;
import ru.finex.core.model.entity.EntityObject;

/**
 * @param <T> entity type
 * @author m0nster.mind
 */
public interface PersistenceService<T extends EntityObject> {

    /**
     * Save or update the entity.
     * @param entity the entity
     * @return updated entity
     */
    T persist(T entity);

    /**
     * Restore the entity from database by game object persistence ID.
     * @param gameObjectPersistenceId game object persistence ID
     * @param entity the default entity
     * @return restored entity or the default entity if entity not found
     * @see #restore(GameObject, EntityObject)
     */
    T restore(int gameObjectPersistenceId, T entity);

    /**
     * Restore the entity from database by the game object persistence ID.
     * @param gameObject the game object
     * @param entity the default entity
     * @return restored entity or the default entity if entity not found
     * @see #restore(int, EntityObject)
     */
    default T restore(GameObject gameObject, T entity) {
        return restore(gameObject.getPersistenceId(), entity);
    }

}

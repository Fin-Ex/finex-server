package ru.finex.core.component;

import ru.finex.core.model.GameObject;
import ru.finex.core.persistence.PersistenceObject;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
public interface Component extends PersistenceObject {

    GameObject getGameObject();

    void setGameObject(GameObject gameObject);

    default boolean isType(Class<?> type) {
        return getClass().isInstance(type);
    }

    default boolean isChildOf(Class<? extends Component> type) {
        return getClass().isAssignableFrom(type);
    }

    @Override
    default int getPersistenceId() {
        return getGameObject().getPersistenceId();
    }
}

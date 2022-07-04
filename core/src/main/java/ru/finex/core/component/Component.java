package ru.finex.core.component;

import ru.finex.core.model.object.GameObject;
import ru.finex.core.persistence.PersistenceObject;

/**
 * Компонент игрового объекта.
 * Является сохраняемой сущностью.
 *
 * @author m0nster.mind
 */
public interface Component extends PersistenceObject {

    /**
     * Возвращает игровой объект (владельца компонента).
     *
     * @return gameObject
     */
    GameObject getGameObject();

    /**
     * Устанавливает владельца компонента.
     *
     * @param gameObject игровой объект
     */
    void setGameObject(GameObject gameObject);

    /**
     * Проверяет тип компонента.
     *
     * @param component проверяемый компонент
     * @return true если текущий тип равен по иерархии или находится выше проверяемого, в ином случае false
     */
    default boolean isType(Component component) {
        return getClass().isInstance(component);
    }

    /**
     * Проверяет тип компонента.
     *
     * @param type проверямый тип компонента
     * @return true если текущий тип наследуется от проверяемого, в ином случае false
     */
    default boolean isChildOf(Class<? extends Component> type) {
        return getClass().isAssignableFrom(type);
    }

    @Override
    default int getPersistenceId() {
        return getGameObject().getPersistenceId();
    }
}

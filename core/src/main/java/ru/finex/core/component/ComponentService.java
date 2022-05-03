package ru.finex.core.component;

import ru.finex.core.model.GameObject;

import java.util.List;

/**
 * Сервис для работы с компонентами.
 *
 * @author m0nster.mind
 */
public interface ComponentService {

    /**
     * Возвращает массив классов компонентов прототипа игрового объекта.
     *
     * @param objectName имя прототипа объекта
     * @return массив классов компонентов
     */
    Class[] getComponentTypesForObject(String objectName);

    /**
     * Добавляет компонент игровому объекту.
     *
     * @param gameObject игровой объект
     * @param component добавляемый компонент
     */
    void addComponent(GameObject gameObject, Component component);

    /**
     * Добавляет компонент игровому объекту.
     *
     * @param gameObject игровой объект
     * @param componentType добавляемый тип компонента
     */
    void addComponent(GameObject gameObject, Class<? extends Component> componentType);

    /**
     * Удаляет компонент у игрового объекта.
     *
     * @param component удаляемый компонент
     * @return true если удаление компонента успешно, в ином случае false
     */
    boolean removeComponent(Component component);

    /**
     * Удаляет компонент у игрового объекта.
     *
     * @param gameObject игровой объект
     * @param componentType удаляемый тип компонента
     * @return true если удаление компонента успешно, в ином случае false
     */
    boolean removeComponent(GameObject gameObject, Class<? extends Component> componentType);

    /**
     * Возвращает компонент игрового объекта.
     *
     * @param gameObject игровой объект
     * @param componentType тип компонента
     * @param <T> генерик тип компонента
     * @return найденный компонент или null если не найдён
     */
    <T extends Component> T getComponent(GameObject gameObject, Class<T> componentType);

    /**
     * Возвращает копию списка компонентов.
     *
     * @param gameObject игровой объект, компоненты которого необходимо получить
     * @return компоненты игрового объекта
     */
    List<Component> getComponents(GameObject gameObject);

}

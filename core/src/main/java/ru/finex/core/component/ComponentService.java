package ru.finex.core.component;

import ru.finex.core.model.GameObject;

import java.util.List;

/**
 * @author m0nster.mind
 */
public interface ComponentService {

    Class[] getComponentTypesForObject(String objectName);

    void addComponent(GameObject gameObject, Component component);
    void addComponent(GameObject gameObject, Class<? extends Component> componentType);

    boolean removeComponent(Component component);
    boolean removeComponent(GameObject gameObject, Class<? extends Component> componentType);

    <T extends Component> T getComponent(GameObject gameObject, Class<T> componentType);

    /**
     * Возвращает копию списка компонентов.
     *
     * @return компоненты игрового объекта
     */
    List<Component> getComponents(GameObject gameObject);



}

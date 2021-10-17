package ru.finex.ws.model.event.component;

import ru.finex.core.component.Component;
import ru.finex.core.model.GameObjectEvent;

/**
 * @author m0nster.mind
 */
public interface ComponentEvent extends GameObjectEvent {

    /**
     * Компонент, который является инициатором события.
     * @return компонент
     */
    Component getComponent();

}

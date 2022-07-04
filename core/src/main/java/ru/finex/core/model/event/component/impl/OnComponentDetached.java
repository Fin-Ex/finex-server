package ru.finex.core.model.event.component.impl;

import lombok.Data;
import ru.finex.core.component.Component;
import ru.finex.core.model.event.component.ComponentEvent;
import ru.finex.core.model.object.GameObject;
import ru.finex.core.pool.PooledObject;

/**
 * Компонент был отключен от игрового объекта.
 *
 * @author m0nster.mind
 */
@Data
@PooledObject(maxSize = "1024")
public class OnComponentDetached implements ComponentEvent {

    private GameObject gameObject;
    private Component component;

    @Override
    public void clear() {
        gameObject = null;
        component = null;
    }

}

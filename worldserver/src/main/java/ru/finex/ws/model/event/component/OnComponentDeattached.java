package ru.finex.ws.model.event.component;

import lombok.Data;
import ru.finex.core.component.Component;
import ru.finex.core.model.GameObject;
import ru.finex.core.pool.PooledObject;

/**
 * Компонент был отключен от игрового объекта.
 *
 * @author m0nster.mind
 */
@Data
@PooledObject(maxSize = 1024)
public class OnComponentDeattached implements ComponentEvent {

    private GameObject gameObject;
    private Component component;

    @Override
    public void clear() {
        gameObject = null;
        component = null;
    }

}

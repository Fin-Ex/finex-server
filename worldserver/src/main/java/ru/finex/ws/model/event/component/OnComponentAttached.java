package ru.finex.ws.model.event.component;

import lombok.Data;
import ru.finex.core.component.Component;
import ru.finex.core.model.GameObject;
import ru.finex.core.pool.PooledObject;

/**
 * Компонент был подключен к игровому объекту. Производится до момента восстановления данных из БД.
 *
 * @author m0nster.mind
 */
@Data
@PooledObject(maxSize = "1024")
public class OnComponentAttached implements ComponentEvent {

    private GameObject gameObject;
    private Component component;

    @Override
    public void clear() {
        gameObject = null;
        component = null;
    }
}

package ru.finex.core.component.event;

import lombok.Data;
import ru.finex.core.component.Component;
import ru.finex.core.pool.PooledObject;

/**
 * Компонент был подключен к игровому объекту. Производится до момента восстановления данных из БД.
 *
 * @author m0nster.mind
 */
@Data
@PooledObject(maxSize = 1024)
public class OnComponentAttached implements ComponentEvent {

    private Component component;

}

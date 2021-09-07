package ru.finex.core.component.event;

import lombok.Data;
import ru.finex.core.component.Component;
import ru.finex.core.pool.PooledObject;

/**
 * Компонент был восстановлен из БД.
 *
 * @author m0nster.mind
 */
@Data
@PooledObject(maxSize = 1024)
public class OnComponentRestored implements ComponentEvent {

    private Component component;

}

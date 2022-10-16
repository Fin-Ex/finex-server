package ru.finex.core.model.event.component;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.finex.core.component.Component;
import ru.finex.core.pool.PooledObject;

/**
 * Компонент был отключен от игрового объекта.
 *
 * @author m0nster.mind
 */
@Data
@NoArgsConstructor
@PooledObject(maxSize = "1024")
public class OnComponentDeattached implements ComponentEvent {

    private int runtimeId;
    private Component component;

    @Override
    public void clear() {
        component = null;
    }

}

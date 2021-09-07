package ru.finex.gs.model;

import com.google.inject.Injector;
import lombok.Getter;
import lombok.Setter;
import ru.finex.core.events.EventBus;
import ru.finex.core.model.GameObject;

/**
 * @author m0nster.mind
 */
public class GameObjectImpl implements GameObject {

    /** Событийная шина игрового объекта. */
    @Getter private final EventBus eventBus = new EventBus();

    /** Динамический ID игрового объекта, который существует только в рамках сессии этого игрового объекта. */
    @Getter private final int runtimeId;
    /** Персистентный ID игрового объекта, который хранится в БД. */
    @Getter private final int persistenceId;
    /** Инжектор, который хранит в себе контекст для инжектирования связанный именно с этим игровым объектом. */
    @Getter @Setter private Injector injector;

    public GameObjectImpl(int runtimeId, int persistenceId) {
        this.runtimeId = runtimeId;
        this.persistenceId = persistenceId;
    }

}

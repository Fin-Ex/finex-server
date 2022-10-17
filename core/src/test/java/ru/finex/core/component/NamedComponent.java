package ru.finex.core.component;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.object.GameObject;
import ru.finex.core.object.GameObjectScoped;
import ru.finex.core.object.impl.GameObjectScope;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * @author m0nster.mind
 */
@GameObjectScoped
public class NamedComponent implements Component {

    @Getter
    @Setter
    @Inject
    private GameObject gameObject;

    @Getter
    @Inject
    @Named(GameObjectScope.RUNTIME_ID)
    private int runtimeId;

    @Getter
    @Inject
    @Named(GameObjectScope.PERSISTENCE_ID)
    private int persistenceId;

}

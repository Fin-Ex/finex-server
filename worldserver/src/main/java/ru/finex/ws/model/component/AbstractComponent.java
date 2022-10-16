package ru.finex.ws.model.component;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.component.Component;
import ru.finex.core.object.GameObject;
import ru.finex.core.object.GameObjectScoped;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@GameObjectScoped
public abstract class AbstractComponent implements Component {

    @Getter
    @Setter
    @Inject
    private GameObject gameObject;

}

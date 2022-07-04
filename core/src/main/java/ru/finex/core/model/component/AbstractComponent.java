package ru.finex.core.model.component;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.component.Component;
import ru.finex.core.model.object.GameObject;

/**
 * @author m0nster.mind
 */
public abstract class AbstractComponent implements Component {

    @Getter
    @Setter
    private GameObject gameObject;

}

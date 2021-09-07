package ru.finex.gs.model.component;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.component.Component;
import ru.finex.core.model.GameObject;

/**
 * @author m0nster.mind
 */
public abstract class AbstractComponent implements Component {

    @Getter @Setter
    private GameObject gameObject;

}

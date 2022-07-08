package ru.finex.core.component;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.component.Component;
import ru.finex.core.object.GameObject;

/**
 * @author m0nster.mind
 */
public abstract class AbstractComponent implements Component {

    @Getter
    @Setter
    private GameObject gameObject;

}

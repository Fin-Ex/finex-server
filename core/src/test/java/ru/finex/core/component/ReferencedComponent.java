package ru.finex.core.component;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.object.GameObject;
import ru.finex.core.object.GameObjectScoped;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@GameObjectScoped
public class ReferencedComponent implements Component {

    @Getter
    @Setter
    @Inject
    private GameObject gameObject;

    @Getter
    @Inject
    private EquipComponent equipComponent;

    @Getter
    @Inject
    private AppearanceComponent appearanceComponent;

}

package ru.finex.core.component;

import lombok.Data;
import ru.finex.core.object.GameObject;
import ru.finex.core.object.GameObjectScoped;

import javax.inject.Inject;

/**
 * @author m0nster.mind
 */
@Data
@GameObjectScoped
public class AppearanceComponent implements Component {

    @Inject
    private GameObject gameObject;

    private String model;

}

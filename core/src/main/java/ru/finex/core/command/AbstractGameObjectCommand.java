package ru.finex.core.command;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.model.GameObject;

/**
 * @author m0nster.mind
 */
public abstract class AbstractGameObjectCommand implements Command {

    @Getter
    @Setter
    private GameObject gameObject;

}

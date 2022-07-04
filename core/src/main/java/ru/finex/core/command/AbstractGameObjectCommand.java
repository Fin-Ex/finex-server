package ru.finex.core.command;

import lombok.Getter;
import lombok.Setter;
import ru.finex.core.model.object.GameObject;

/**
 * Абстрактная команда игровой логики.
 * Инкапсулирует внутри себя игровой объект на который направлено действие.
 *
 * @author m0nster.mind
 */
public abstract class AbstractGameObjectCommand implements Command {

    @Getter
    @Setter
    private GameObject gameObject;

}

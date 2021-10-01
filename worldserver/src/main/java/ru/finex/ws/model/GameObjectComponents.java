package ru.finex.ws.model;

import lombok.Data;
import ru.finex.core.component.Component;

import java.util.ArrayList;

/**
 * @author m0nster.mind
 */
@Data
public class GameObjectComponents {

    /** Динамический ID игрового объекта, которому принадлежат компоненты. */
    private final int runtimeId;
    private final ArrayList<Component> components = new ArrayList<>();

}

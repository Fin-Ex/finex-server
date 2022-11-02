package ru.finex.core.component.prototype;

import lombok.Data;
import ru.finex.core.prototype.ComponentPrototype;

/**
 * @author m0nster.mind
 */
@Data
public class AppearancePrototype implements ComponentPrototype {

    private String type;
    private String model;

}

package ru.finex.core.prototype;

import com.google.inject.ImplementedBy;
import ru.finex.core.prototype.impl.GameObjectPrototypeServiceImpl;

import java.util.List;

/**
 * @author m0nster.mind
 */
@ImplementedBy(GameObjectPrototypeServiceImpl.class)
public interface GameObjectPrototypeService {

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    List<ComponentPrototype> getPrototypesByName(String prototypeName);

}

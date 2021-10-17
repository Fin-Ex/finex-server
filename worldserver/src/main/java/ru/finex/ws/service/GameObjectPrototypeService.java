package ru.finex.ws.service;

import com.google.inject.ImplementedBy;
import ru.finex.ws.service.impl.GameObjectPrototypeServiceImpl;

import java.util.List;

/**
 * @author m0nster.mind
 */
@ImplementedBy(GameObjectPrototypeServiceImpl.class)
public interface GameObjectPrototypeService {

    @SuppressWarnings("checkstyle:MissingJavadocMethod")
    List<String> getComponentsByPrototypeName(String objectName);

}

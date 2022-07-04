package ru.finex.core.service.object;

import com.google.inject.ImplementedBy;
import com.google.inject.Injector;
import ru.finex.core.model.object.GameObject;
import ru.finex.core.service.object.impl.GameObjectInjectorServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(GameObjectInjectorServiceImpl.class)
public interface GameObjectInjectorService {

    /**
     * Create a new game object injector.
     * @param gameObject game object
     * @return injector
     */
    Injector create(GameObject gameObject);

    /**
     * Search injector for specified game object.
     * @param goRuntimeId game object runtime ID
     * @return injector or null if not found
     */
    Injector getInjector(int goRuntimeId);

    /**
     * Search injector for specified game object, if injector not found for specified
     *  game object create a new one.
     * @param gameObject game object
     * @return injector
     */
    Injector getOrCreate(GameObject gameObject);

}

package ru.finex.ws.service;

import com.google.inject.ImplementedBy;
import com.google.inject.Injector;
import ru.finex.core.model.GameObject;
import ru.finex.ws.service.impl.GameObjectInjectorServiceImpl;

/**
 * @author m0nster.mind
 */
@ImplementedBy(GameObjectInjectorServiceImpl.class)
public interface GameObjectInjectorService {

    Injector create(GameObject gameObject);
    Injector getInjector(int goRuntimeId);
    Injector getOrCreate(GameObject gameObject);

}

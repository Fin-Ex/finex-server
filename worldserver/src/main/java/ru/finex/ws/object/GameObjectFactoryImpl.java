package ru.finex.ws.object;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.GameObject;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.ws.model.GameObjectImpl;
import ru.finex.ws.service.GameObjectInjectorService;
import ru.finex.ws.service.RuntimeIdService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author finfan
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectFactoryImpl implements GameObjectFactory {

    private final RuntimeIdService runtimeIdService;
    private final GameObjectPersistenceService persistenceService;
    private final ComponentService componentService;
    private final GameObjectInjectorService injectorService;

    @Override
    public GameObject createGameObject(String templateName, int persistenceId) {
        int runtimeId = runtimeIdService.generateId();

        GameObjectImpl gameObject = new GameObjectImpl(runtimeId, persistenceId);
        injectorService.create(gameObject);
        componentService.addComponentsFromPrototype(templateName, gameObject);
        persistenceService.restore(gameObject);

        return gameObject;
    }

}

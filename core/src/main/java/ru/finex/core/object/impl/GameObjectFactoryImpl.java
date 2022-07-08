package ru.finex.core.object.impl;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.ComponentService;
import ru.finex.core.id.RuntimeIdService;
import ru.finex.core.model.object.scope.GameObjectScope;
import ru.finex.core.object.GameObject;
import ru.finex.core.object.GameObjectFactory;
import ru.finex.core.persistence.GameObjectPersistenceService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author finfan
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectFactoryImpl implements GameObjectFactory {

    private final GameObjectScope<GameObject> gameObjectScope;

    private final RuntimeIdService runtimeIdService;
    private final GameObjectPersistenceService persistenceService;
    private final ComponentService componentService;

    @Override
    public GameObject createGameObject(String templateName, int persistenceId) {
        int runtimeId = runtimeIdService.generateId();

        GameObjectImpl gameObject = new GameObjectImpl(runtimeId, persistenceId);
        try {
            gameObjectScope.enterScope(gameObject);
            componentService.addComponentsFromPrototype(templateName, gameObject);
            persistenceService.restore(gameObject);
        } finally {
            gameObjectScope.exitScope();
        }

        return gameObject;
    }

}

package ru.finex.ws.player;

import com.google.inject.Module;
import lombok.RequiredArgsConstructor;
import ru.finex.core.ServerContext;
import ru.finex.core.component.Component;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.GameObject;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.core.utils.InjectorUtils;
import ru.finex.ws.inject.GameplayModule;
import ru.finex.ws.inject.module.gameplay.GameObjectModule;
import ru.finex.ws.model.GameObjectImpl;
import ru.finex.ws.service.RuntimeIdService;

import java.util.List;
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
    private final ServerContext ctx;

    @Override
    public GameObject createGameObject(String templateName, int persistenceId) {
        int runtimeId = runtimeIdService.generateId();

        GameObjectImpl gameObject = new GameObjectImpl(runtimeId, persistenceId);
        setupInjector(gameObject);
        setupComponents(templateName, gameObject);
        persistenceService.restore(gameObject);

        return gameObject;
    }

    private void setupInjector(GameObjectImpl gameObject) {
        List<Module> modules = InjectorUtils.collectModules(GameplayModule.class);
        modules.add(new GameObjectModule(gameObject));
        gameObject.setInjector(ctx.getInjector().createChildInjector(modules));
    }

    private void setupComponents(String templateName, GameObject gameObject) {
        Class[] types = componentService.getComponentTypesForObject(templateName);
        for (int i = 0; i < types.length; i++) {
            Class<? extends Component> componentType = types[i];
            componentService.addComponent(gameObject, componentType);
        }
    }

}

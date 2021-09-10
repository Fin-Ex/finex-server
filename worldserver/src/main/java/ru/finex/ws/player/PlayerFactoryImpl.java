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
import ru.finex.ws.inject.module.gameplay.PlayerModule;
import ru.finex.ws.model.Client;
import ru.finex.ws.model.GameObjectImpl;
import ru.finex.ws.service.RuntimeIdService;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * FIXME m0nster.mind: обощить до GameObject
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class PlayerFactoryImpl implements PlayerFactory {

    private final RuntimeIdService runtimeIdService;
    private final GameObjectPersistenceService persistenceService;
    private final ComponentService componentService;
    private final ServerContext ctx;

    @Override
    public GameObject createPlayer(Client client, int persistenceId) {
        int runtimeId = runtimeIdService.generateId();

        GameObjectImpl gameObject = new GameObjectImpl(runtimeId, persistenceId);
        setupInjector(client, gameObject);
        setupComponents(gameObject);
        persistenceService.restore(gameObject);

        return gameObject;
    }

    private void setupInjector(Client client, GameObjectImpl gameObject) {
        List<Module> modules = InjectorUtils.collectModules(GameplayModule.class);
        modules.add(new PlayerModule(client));
        modules.add(new GameObjectModule(gameObject));
        gameObject.setInjector(ctx.getInjector().createChildInjector(modules));
    }

    private void setupComponents(GameObject gameObject) {
        Class[] types = componentService.getComponentTypesForObject("player");
        for (int i = 0; i < types.length; i++) {
            Class<? extends Component> componentType = types[i];
            componentService.addComponent(gameObject, componentType);
        }
    }

}

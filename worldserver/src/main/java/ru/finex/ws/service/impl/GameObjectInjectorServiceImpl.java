package ru.finex.ws.service.impl;

import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Module;
import lombok.RequiredArgsConstructor;
import ru.finex.core.GlobalContext;
import ru.finex.core.cluster.impl.Clustered;
import ru.finex.core.events.ClusterSubscription;
import ru.finex.core.events.cluster.ClusterEventBus;
import ru.finex.core.model.GameObject;
import ru.finex.core.model.GameObjectEvent;
import ru.finex.core.utils.InjectorUtils;
import ru.finex.ws.inject.GameplayModule;
import ru.finex.ws.inject.module.gameplay.GameObjectModule;
import ru.finex.ws.model.GameObjectImpl;
import ru.finex.ws.model.event.GameObjectDestroyed;
import ru.finex.ws.service.GameObjectInjectorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectInjectorServiceImpl implements GameObjectInjectorService {

    private final Map<Integer, Injector> injectors = new HashMap<>();
    private ClusterSubscription<GameObjectEvent> eventSubscription;

    public void subscribe(@Clustered(GameObjectEvent.CHANNEL) ClusterEventBus<GameObjectEvent> eventBus) {
        eventSubscription = eventBus.subscribe();
    }

    @Override
    public Injector create(GameObject gameObject) {
        GameObjectImpl go = (GameObjectImpl) gameObject;
        List<Module> modules = InjectorUtils.collectModules(GameplayModule.class);
        modules.add(new GameObjectModule(gameObject));
        Injector injector = GlobalContext.injector.createChildInjector(modules);
        injectors.put(go.getRuntimeId(), injector);
        return injector;
    }

    @Override
    public Injector getOrCreate(GameObject gameObject) {
        Injector injector = getInjector(gameObject.getRuntimeId());
        if (injector == null) {
            injector = create(gameObject);
        }

        Objects.requireNonNull(injector, "Fail to create injector!");

        return injector;
    }

    @Override
    public Injector getInjector(int goRuntimeId) {
        return injectors.get(goRuntimeId);
    }

    public void onInputProcessing() { // FIXME m0nster.mind: call at INPUT
        eventSubscription.processEvents(this::processGameObjectEvent);
    }

    private void processGameObjectEvent(GameObjectEvent event) {
        if (event instanceof GameObjectDestroyed e) {
            injectors.remove(e.getGameObject().getRuntimeId());
        }
    }

}
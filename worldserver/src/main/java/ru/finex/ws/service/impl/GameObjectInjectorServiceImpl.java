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
import ru.finex.ws.tick.RegisterTick;
import ru.finex.ws.tick.TickStage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class GameObjectInjectorServiceImpl implements GameObjectInjectorService {

    private final Map<Integer, Injector> injectors = new HashMap<>();
    private ClusterSubscription<GameObjectEvent> eventSubscription;

    public void subscribe(@Clustered(GameObjectEvent.CHANNEL) ClusterEventBus<GameObjectEvent> eventBus) {
        eventSubscription = eventBus.subscribe();
    }

    @Override
    public Injector create(GameObject gameObject) {
        // TODO m0nster.mind: здесь надо создавать не новый инжектор, а принципиально переделать логику
        //  необходимо создавать скоуп игрового объекта и в фактори, при инжектировании, входить в него
        //  таким образом мы избавляемся от отдельных инжекторов - что является огромным расходом памяти,
        //  а также процессорного времени на его создание.
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

    @RegisterTick(TickStage.PRE_INPUT)
    public void onInputProcessing() {
        eventSubscription.processEvents(this::processGameObjectEvent);
    }

    private void processGameObjectEvent(GameObjectEvent event) {
        if (event instanceof GameObjectDestroyed e) {
            injectors.remove(e.getGameObject().getRuntimeId());
        }
    }

}

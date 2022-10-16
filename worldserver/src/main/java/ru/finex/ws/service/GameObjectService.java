package ru.finex.ws.service;

import com.google.inject.ImplementedBy;
import ru.finex.core.events.cluster.ClusterEventBus;
import ru.finex.core.model.event.GameObjectEvent;
import ru.finex.core.object.GameObject;
import ru.finex.ws.service.impl.GameObjectServiceImpl;

/**
 * @author m0nster.mind
 */
@SuppressWarnings("checkstyle:MissingJavadocMethod")
@ImplementedBy(GameObjectServiceImpl.class)
public interface GameObjectService {

    ClusterEventBus<GameObjectEvent> getEventBus();

    GameObject createGameObject(String template, int persistenceId);

    void destroyObject(GameObject gameObject);

    GameObject getGameObject(int runtimeId);

}

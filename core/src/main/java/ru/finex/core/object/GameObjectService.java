package ru.finex.core.object;

import com.google.inject.ImplementedBy;
import ru.finex.core.events.cluster.ClusterEventBus;
import ru.finex.core.model.event.object.GameObjectEvent;
import ru.finex.core.object.impl.GameObjectServiceImpl;

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

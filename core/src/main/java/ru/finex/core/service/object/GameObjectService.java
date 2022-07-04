package ru.finex.core.service.object;

import com.google.inject.ImplementedBy;
import ru.finex.core.events.cluster.ClusterEventBus;
import ru.finex.core.model.GameObjectEvent;
import ru.finex.core.model.object.GameObject;
import ru.finex.core.service.object.impl.GameObjectServiceImpl;

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

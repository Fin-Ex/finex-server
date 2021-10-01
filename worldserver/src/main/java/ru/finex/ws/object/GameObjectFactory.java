package ru.finex.ws.object;

import com.google.inject.ImplementedBy;
import ru.finex.core.model.GameObject;

/**
 * @author finfan
 */
@ImplementedBy(GameObjectFactoryImpl.class)
public interface GameObjectFactory {

    GameObject createGameObject(String templateName, int persistenceId);

}

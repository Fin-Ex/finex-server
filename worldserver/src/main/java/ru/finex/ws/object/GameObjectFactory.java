package ru.finex.ws.object;

import com.google.inject.ImplementedBy;
import ru.finex.core.model.GameObject;

/**
 * @author finfan
 */
@ImplementedBy(GameObjectFactoryImpl.class)
public interface GameObjectFactory {

    /**
     * Create a new game object from prototype.
     * <p>
     * <img src="doc-files/GameObjectFactory.createGameObject.png" alt=""/>
     * @param templateName game object prototype name
     * @param persistenceId persistence ID, can be -1 if undefined
     * @return game object
     */
    GameObject createGameObject(String templateName, int persistenceId);

}

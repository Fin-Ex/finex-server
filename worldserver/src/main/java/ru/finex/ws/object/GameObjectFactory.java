package ru.finex.ws.object;

import com.google.inject.ImplementedBy;
import ru.finex.core.model.GameObject;

/**
 * @author finfan
 */
@ImplementedBy(GameObjectFactoryImpl.class)
public interface GameObjectFactory {

    /**
     * Create a new game object by prototype.
     * <img src="doc-files/GameObjectFactory-createGameObject.png"/>
     * @startuml doc-files/GameObjectFactory-createGameObject.png
     * actor Developer as actor
     * participant "Object Factory" as factory
     * participant "Component Service" as service
     * participant "Prototype Service" as protoservice
     * participant Mapper as mapper
     * participant Repository as repo
     * database DB as db
     *
     * actor->factory: Create a new GameObject
     * factory->service: Get components for GameObject
     * service->protoservice: Get components prototypes for GameObject
     * protoservice->repo: Query to fetch prototypes
     * repo->db: Recursive fetch prototypes
     * db-->repo: Return entities
     * repo-->protoservice: Return entities
     * protoservice-->service: Map entities to ComponentPrototype
     * service->mapper: Map prototypes to components
     * mapper-->service: Return component
     * service-->factory: Inject dependencies into components\n and return components
     * factory-->actor: Allocate new GameObject\n and attach components to it
     * @enduml
     * @param templateName game object prototype name
     * @param persistenceId persistence ID, can be -1 to undefined
     * @return game object
     */
    GameObject createGameObject(String templateName, int persistenceId);

}

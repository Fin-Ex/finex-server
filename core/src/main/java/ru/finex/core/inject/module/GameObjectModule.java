package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.core.model.object.DefaultGameObjectScope;
import ru.finex.core.model.object.GameObjectScope;
import ru.finex.core.model.object.GameObjectScoped;

/**
 * @author oracle
 */
public class GameObjectModule extends AbstractModule {

    @Override
    protected void configure() {
        var gameObjectScope = new DefaultGameObjectScope();

        bindScope(GameObjectScoped.class, gameObjectScope);
        bind(GameObjectScope.class).toInstance(gameObjectScope);
    }

}

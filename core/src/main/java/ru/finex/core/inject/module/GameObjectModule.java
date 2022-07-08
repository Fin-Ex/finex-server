package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import ru.finex.core.component.ComponentService;
import ru.finex.core.model.object.scope.GameObjectScope;
import ru.finex.core.model.object.scope.GameObjectScoped;
import ru.finex.core.model.object.scope.impl.DefaultGameObjectScope;
import ru.finex.core.object.GameObject;

/**
 * @author oracle
 */
public class GameObjectModule extends AbstractModule {

    @Override
    protected void configure() {
        var gameObjectScope = new DefaultGameObjectScope(getProvider(ComponentService.class));

        bindScope(GameObjectScoped.class, gameObjectScope);
        bind(new TypeLiteral<GameObjectScope<GameObject>>() { }).toInstance(gameObjectScope);
    }

}

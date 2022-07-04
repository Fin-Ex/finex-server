package ru.finex.core.inject.module.gameobject;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import lombok.RequiredArgsConstructor;
import ru.finex.core.component.inject.GameObjectComponentModule;
import ru.finex.core.model.object.GameObject;
import ru.finex.core.model.object.scope.GameObjectScope;
import ru.finex.core.model.object.scope.GameObjectScoped;
import ru.finex.core.model.object.scope.impl.DefaultGameObjectScope;

/**
 * @author oracle
 */
@RequiredArgsConstructor
public class GameObjectModule extends AbstractModule {

    private final GameObject gameObject;

    @Override
    protected void configure() {
        var gameObjectScope = new DefaultGameObjectScope();

        bindScope(GameObjectScoped.class, gameObjectScope);
        bind(new TypeLiteral<GameObjectScope<GameObject>>() { }).toInstance(gameObjectScope);

        install(new GameObjectComponentModule(gameObject));
    }

}

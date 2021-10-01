package ru.finex.ws.inject.module.gameplay;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import ru.finex.core.model.GameObject;
import ru.finex.ws.component.inject.GameObjectComponentModule;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class GameObjectModule extends AbstractModule {

    private final GameObject gameObject;

    @Override
    protected void configure() {
        install(new GameObjectComponentModule(gameObject));
    }
}

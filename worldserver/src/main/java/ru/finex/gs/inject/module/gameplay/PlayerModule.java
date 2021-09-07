package ru.finex.gs.inject.module.gameplay;

import com.google.inject.AbstractModule;
import lombok.RequiredArgsConstructor;
import ru.finex.core.model.GameObject;
import ru.finex.gs.model.Client;

import javax.inject.Provider;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class PlayerModule extends AbstractModule {

    private final Client client;

    @Override
    protected void configure() {
        bind(Client.class).toInstance(client);
        bind(GameObject.class).toProvider((Provider<GameObject>) client::getGameObject);
    }
}

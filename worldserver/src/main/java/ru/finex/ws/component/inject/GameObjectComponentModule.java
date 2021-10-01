package ru.finex.ws.component.inject;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import lombok.RequiredArgsConstructor;
import ru.finex.core.model.GameObject;

/**
 * @author m0nster.mind
 */
@RequiredArgsConstructor
public class GameObjectComponentModule extends AbstractModule {

    private final GameObject gameObject;

    @Override
    protected void configure() {
        bindListener(Matchers.any(), new ComponentTypeListener(gameObject));
    }

}

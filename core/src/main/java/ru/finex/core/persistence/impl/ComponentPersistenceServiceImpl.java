package ru.finex.core.persistence.impl;

import lombok.RequiredArgsConstructor;
import ru.finex.core.component.Component;
import ru.finex.core.component.event.OnComponentRestored;
import ru.finex.core.model.GameObject;
import ru.finex.core.persistence.ComponentPersistenceService;
import ru.finex.core.pool.PoolService;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * @author m0nster.mind
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = { @Inject })
public class ComponentPersistenceServiceImpl extends ObjectPersistenceServiceImpl implements ComponentPersistenceService {

    private final PoolService poolService;

    @Override
    public void persist(Component component) {
        super.persist(component);
    }

    @Override
    public void restore(Component component) {
        super.restore(component);
        notifyOnRestored(component.getGameObject(), component);
    }

    private void notifyOnRestored(GameObject gameObject, Component component) {
        OnComponentRestored event = poolService.getObject(OnComponentRestored.class);
        event.setComponent(component);
        gameObject.getEventBus().notify(event);
    }

}

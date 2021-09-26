package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.core.persistence.ComponentPersistenceService;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.core.persistence.ObjectPersistenceService;
import ru.finex.core.persistence.impl.ComponentPersistenceServiceImpl;
import ru.finex.core.persistence.impl.GameObjectPersistenceServiceImpl;
import ru.finex.core.persistence.impl.ObjectPersistenceServiceImpl;

/**
 * @author m0nster.mind
 */
public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectPersistenceService.class).to(ObjectPersistenceServiceImpl.class);
        bind(ComponentPersistenceService.class).to(ComponentPersistenceServiceImpl.class);
        bind(GameObjectPersistenceService.class).to(GameObjectPersistenceServiceImpl.class);
    }

}

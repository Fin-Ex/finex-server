package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.persistence.impl.ComponentPersistenceServiceImpl;
import ru.finex.core.persistence.impl.GameObjectPersistenceServiceImpl;
import ru.finex.core.persistence.impl.ObjectPersistenceServiceImpl;
import ru.finex.core.persistence.ComponentPersistenceService;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.core.persistence.ObjectPersistenceService;

/**
 * @author m0nster.mind
 */
@LoaderModule
public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(ObjectPersistenceService.class).to(ObjectPersistenceServiceImpl.class);
        bind(ComponentPersistenceService.class).to(ComponentPersistenceServiceImpl.class);
        bind(GameObjectPersistenceService.class).to(GameObjectPersistenceServiceImpl.class);
    }

}

package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import ru.finex.core.GlobalContext;
import ru.finex.core.model.entity.EntityObject;
import ru.finex.core.persistence.GameObjectPersistenceService;
import ru.finex.core.persistence.ObjectPersistenceService;
import ru.finex.core.persistence.PersistenceService;
import ru.finex.core.persistence.impl.GameObjectPersistenceServiceImpl;
import ru.finex.core.persistence.impl.ObjectPersistenceServiceImpl;
import ru.finex.core.utils.GenericUtils;

import java.lang.reflect.Modifier;

/**
 * @author m0nster.mind
 */
public class PersistenceModule extends AbstractModule {

    @Override
    protected void configure() {
        bindPersistenceServices();
        bind(ObjectPersistenceService.class).to(ObjectPersistenceServiceImpl.class);
        bind(GameObjectPersistenceService.class).to(GameObjectPersistenceServiceImpl.class);
    }

    private void bindPersistenceServices() {
        var mapBinder = MapBinder.newMapBinder(
            binder(),
            new TypeLiteral<Class<? extends EntityObject>>() { },
            new TypeLiteral<PersistenceService>() { },
            Names.named("PersistenceServices")
        );

        GlobalContext.reflections.getSubTypesOf(PersistenceService.class)
            .stream()
            .filter(e -> !Modifier.isAbstract(e.getModifiers()))
            .forEach(e -> mapBinder.addBinding(GenericUtils.getInterfaceGenericType(e, PersistenceService.class, 0))
                .to(e)
                .in(Scopes.SINGLETON)
            );
    }

}

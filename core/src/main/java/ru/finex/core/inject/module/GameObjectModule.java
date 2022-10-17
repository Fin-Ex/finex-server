package ru.finex.core.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import ru.finex.core.GlobalContext;
import ru.finex.core.component.ComponentService;
import ru.finex.core.component.impl.ComponentServiceImpl;
import ru.finex.core.object.GameObject;
import ru.finex.core.object.GameObjectFactory;
import ru.finex.core.object.GameObjectScoped;
import ru.finex.core.object.impl.GameObjectFactoryImpl;
import ru.finex.core.object.impl.GameObjectScope;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.core.utils.GenericUtils;

import java.util.Objects;

/**
 * @author m0nster.mind
 */
public class GameObjectModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(GameObjectFactory.class).to(GameObjectFactoryImpl.class);
        bind(ComponentService.class).to(ComponentServiceImpl.class);
        bindPrototypeMappers();

        var gameObjectScope = new GameObjectScope(getProvider(ComponentService.class));
        bindScope(GameObjectScoped.class, gameObjectScope);
        bind(GameObjectScope.class).toInstance(gameObjectScope);
        bind(GameObject.class).toProvider(gameObjectScope.gameObjectProvider()).in(GameObjectScoped.class);
        bind(Integer.class).annotatedWith(Names.named(GameObjectScope.RUNTIME_ID))
                .toProvider(gameObjectScope.runtimeIdProvider()).in(GameObjectScoped.class);
        bind(Integer.class).annotatedWith(Names.named(GameObjectScope.PERSISTENCE_ID))
                .toProvider(gameObjectScope.persistenceIdProvider()).in(GameObjectScoped.class);
    }

    private void bindPrototypeMappers() {
        var mapperBinder = MapBinder.newMapBinder(
                binder(),
                new TypeLiteral<Class<? extends ComponentPrototype>>() { },
                new TypeLiteral<ComponentPrototypeMapper>() { },
                Names.named("ComponentMappers")
        );

        GlobalContext.reflections.getSubTypesOf(ComponentPrototypeMapper.class)
                .stream()
                .filter(mapper -> !mapper.isAnnotationPresent(ComponentPrototypeMapper.Ignore.class))
                .forEach(mapper -> registerMapper(mapper, mapperBinder));
    }

    @SneakyThrows
    private void registerMapper(
            Class<? extends ComponentPrototypeMapper> mapperType,
            MapBinder<Class<? extends ComponentPrototype>, ComponentPrototypeMapper> mapperBinder) {
        var registrations = mapperType.getAnnotationsByType(ComponentPrototypeMapper.Register.class);
        if (registrations.isEmpty()) {
            mapperBinder.addBinding(GenericUtils.getInterfaceGenericType(mapperType, ComponentPrototypeMapper.class, 0))
                    .to(mapperType);
        } else {
            var ctor = ConstructorUtils.getAccessibleConstructor(mapperType, Class.class);
            if (ctor == null) {
                ctor = ConstructorUtils.getAccessibleConstructor(mapperType);
                Objects.requireNonNull(ctor, String.format(
                        "Not found usable constructor for class '%s' marked as @Register",
                        mapperType.getCanonicalName()
                ));
            }
            for (var registration : registrations) {
                ComponentPrototypeMapper mapper = ctor.newInstance(registration.component());
                requestInjection(mapper);
                mapperBinder.addBinding(registration.prototype()).toInstance(mapper);
            }
        }
    }

}

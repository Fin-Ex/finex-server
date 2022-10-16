package ru.finex.ws.inject.module;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.multibindings.MapBinder;
import com.google.inject.name.Names;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import ru.finex.core.GlobalContext;
import ru.finex.core.component.ComponentService;
import ru.finex.core.component.impl.ComponentServiceImpl;
import ru.finex.core.inject.LoaderModule;
import ru.finex.core.inject.module.ClusterModule;
import ru.finex.core.inject.module.ClusteredUidModule;
import ru.finex.core.inject.module.DbModule;
import ru.finex.core.inject.module.HoconModule;
import ru.finex.core.inject.module.ManagementModule;
import ru.finex.core.inject.module.NetworkModule;
import ru.finex.core.inject.module.PersistenceModule;
import ru.finex.core.inject.module.PlaceholderJuelModule;
import ru.finex.core.inject.module.PoolModule;
import ru.finex.core.prototype.ComponentPrototype;
import ru.finex.core.prototype.ComponentPrototypeMapper;
import ru.finex.core.utils.GenericUtils;
import ru.finex.ws.tick.TickService;
import ru.finex.ws.tick.impl.RegisterTickListener;
import ru.finex.ws.tick.impl.TickServiceImpl;

import java.util.Objects;

/**
 * @author m0nster.mind
 */
@LoaderModule
@EqualsAndHashCode(callSuper = false)
public class WorldServerModule extends AbstractModule {

    @Override
    protected void configure() {
        install(new DbModule());
        install(new HoconModule());
        install(new PlaceholderJuelModule());
        install(new ClusterModule());
        install(new ClusteredUidModule());
        install(new PersistenceModule());
        install(new PoolModule());
        install(new NetworkModule());
        install(new CommandModule());
        install(new ManagementModule());
        bindPrototypeMappers();
        bind(ComponentService.class).to(ComponentServiceImpl.class);
        bind(TickService.class).to(TickServiceImpl.class);
        bindListener(Matchers.any(), new RegisterTickListener());
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
